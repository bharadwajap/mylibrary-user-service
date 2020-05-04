package com.mylibrary.user.rest.advices;

import com.mylibrary.user.dto.ProblemDetail;
import com.mylibrary.user.exceptions.ConstraintViolationException;
import com.mylibrary.user.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Allows to handle all expected and unexpected errors occurred while processing the request.
 *
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    private static final String APPLICATION_PROBLEM_JSON = "application/problem+json";

    /**
     * Handles a case when requested resource cannot be found
     *
     * @param e any exception of type {@link Exception}
     * @return {@link ResponseEntity} containing standard body in case of errors
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public HttpEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException e,
                                                                     final HttpServletRequest request) {

        LOGGER.debug("Resource {} of type {} cannot be found", e.getResourceId(), e.getResourceType());

        ProblemDetail problem = new ProblemDetail("Resource not found", "Requested resource cannot be found");
        problem.setStatus(HttpStatus.NOT_FOUND.value());
        problem.setInstance(request.getRequestURI());

        return new ResponseEntity<>(problem, overrideContentType(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles a case when message from request body cannot be de-serialized
     *
     * @param e any exception of type {@link HttpMessageNotReadableException}
     * @return {@link ResponseEntity} containing standard body in case of errors
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public HttpEntity<ProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                                           final HttpServletRequest request) {

        LOGGER.debug("Cannot de-serialize message in request body: {}", e.getMessage());

        final ProblemDetail problem = new ProblemDetail("Message cannot be converted",
                String.format("Invalid request body: %s", e.getMessage()));
        problem.setStatus(HttpStatus.BAD_REQUEST.value());
        problem.setInstance(request.getRequestURI());

        return new ResponseEntity<>(problem, overrideContentType(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles a case when validation of the request body fails
     *
     * @param e any exception of type {@link MethodArgumentNotValidException}
     * @return {@link ResponseEntity} containing standard body in case of errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                           final HttpServletRequest request) {

        LOGGER.debug("Request body is invalid: {}", e.getMessage());

        final ProblemDetail problem = new ProblemDetail("Validation failed", null);
        problem.setStatus(HttpStatus.BAD_REQUEST.value());
        problem.setInstance(request.getRequestURI());

        problem.setErrors(e.getBindingResult().getAllErrors().stream()
                .map(objectError -> new ProblemDetail("Invalid Parameter", wrapWithFieldName(objectError)))
                .collect(Collectors.toList()));

        return new ResponseEntity<>(problem, overrideContentType(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles a case when validation of the request body fails
     *
     * @param e any exception of type {@link MethodArgumentTypeMismatchException}
     * @return {@link ResponseEntity} containing standard body in case of errors
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public HttpEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                                               final HttpServletRequest request) {

        LOGGER.debug("Request body is invalid: {}", e.getMessage());

        final ProblemDetail problem = new ProblemDetail("Field type mismatch", null);
        problem.setStatus(HttpStatus.BAD_REQUEST.value());
        problem.setInstance(request.getRequestURI());
        problem.setErrors(Collections.singletonList(
                new ProblemDetail("Wrong field value format",
                        String.format("Incorrect value '%s' for field '%s'. Expected value type '%s'", e.getValue(), e.getName(), e.getParameter().getParameterType().getTypeName()))));

        return new ResponseEntity<>(problem, overrideContentType(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles a case when some constraint violation has occurred
     *
     * @param e any exception of type {@link ConstraintViolationException}
     * @return {@link ResponseEntity} containing standard body in case of errors
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public HttpEntity<ProblemDetail> handleConstraintViolations(ConstraintViolationException e,
                                                                final HttpServletRequest request) {

        LOGGER.debug("Constraint Violation: {}", e.getMessage());

        final ProblemDetail problem = new ProblemDetail("Constraint Violation", e.getMessage());
        problem.setStatus(HttpStatus.CONFLICT.value());
        problem.setInstance(request.getRequestURI());

        return new ResponseEntity<>(problem, overrideContentType(), HttpStatus.CONFLICT);
    }


    /**
     * Handles all unexpected situations
     *
     * @param e any exception of type {@link Exception}
     * @return {@link ResponseEntity} containing standard body in case of errors
     */
    @ExceptionHandler(Exception.class)
    public HttpEntity<ProblemDetail> handleException(Exception e,
                                                     final HttpServletRequest request) {

        LOGGER.error("An unexpected error occurred", e);

        ProblemDetail problem = new ProblemDetail("Internal Error", "An unexpected error has occurred");
        problem.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        problem.setInstance(request.getRequestURI());

        return new ResponseEntity<>(problem, overrideContentType(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpHeaders overrideContentType() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", APPLICATION_PROBLEM_JSON);
        return httpHeaders;
    }

    private String wrapWithFieldName(ObjectError objectError) {
        String defaultMessage = objectError.getDefaultMessage();
        //put field name in case of Field Error
        if (objectError instanceof FieldError) {
            defaultMessage = ((FieldError) objectError).getField() + " " + objectError.getDefaultMessage();
        }
        return defaultMessage;
    }
}