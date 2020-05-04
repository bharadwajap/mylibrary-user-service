package com.mylibrary.user.rest.advices;

import com.mylibrary.user.dto.ProblemDetail;
import com.mylibrary.user.exceptions.ConstraintViolationException;
import com.mylibrary.user.exceptions.ResourceNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Makes sure that error handling is done according to API guidelines {@link ExceptionControllerAdvice}.
 * Format of the error response - Problem Detail.
 *
 * @author Vadym Lotar
 * @see ExceptionControllerAdvice
 * @since 0.1.0.RELEASE
 */
public class ExceptionControllerAdviceTest {

    private ExceptionControllerAdvice advice;

    private HttpServletRequest httpServletRequest;

    @Before
    public void setUp() throws Exception {
        this.advice = new ExceptionControllerAdvice();
        this.httpServletRequest = Mockito.mock(HttpServletRequest.class);
    }

    @Test
    public void testHandleResourceNotFoundException() throws Exception {
        ResourceNotFoundException exception = new ResourceNotFoundException("Book", "1244213");
        when(this.httpServletRequest.getRequestURI()).thenReturn("/books/" + exception.getResourceId());

        ProblemDetail problemDetail = this.advice.handleResourceNotFoundException(exception, this.httpServletRequest).getBody();
        Assert.assertEquals(new Integer(404), problemDetail.getStatus());
        Assert.assertEquals("/books/" + exception.getResourceId(), problemDetail.getInstance());
        Assert.assertEquals("Resource not found", problemDetail.getTitle());
        Assert.assertEquals("Requested resource cannot be found", problemDetail.getDetail());
        Assert.assertNull(problemDetail.getType());
        Assert.assertNull(problemDetail.getErrors());

        verify(this.httpServletRequest, times(1)).getRequestURI();
        verifyNoMoreInteractions(this.httpServletRequest);
    }

    @Test
    public void testHandleException() throws Exception {
        when(this.httpServletRequest.getRequestURI()).thenReturn("/books/1");

        ProblemDetail problemDetail = this.advice.handleException(new RuntimeException(), this.httpServletRequest).getBody();
        Assert.assertEquals(new Integer(500), problemDetail.getStatus());
        Assert.assertEquals("/books/1", problemDetail.getInstance());
        Assert.assertEquals("Internal Error", problemDetail.getTitle());
        Assert.assertEquals("An unexpected error has occurred", problemDetail.getDetail());
        Assert.assertNull(problemDetail.getType());
        Assert.assertNull(problemDetail.getErrors());

        verify(this.httpServletRequest, times(1)).getRequestURI();
        verifyNoMoreInteractions(this.httpServletRequest);
    }

    @Test
    public void testHandleHttpMessageNotReadableException() throws Exception {
        when(this.httpServletRequest.getRequestURI()).thenReturn("/books/1");

        ProblemDetail problemDetail = this.advice.handleHttpMessageNotReadableException(
                new HttpMessageNotReadableException("\" is missing"), this.httpServletRequest).getBody();
        Assert.assertEquals(new Integer(400), problemDetail.getStatus());
        Assert.assertEquals("/books/1", problemDetail.getInstance());
        Assert.assertEquals("Message cannot be converted", problemDetail.getTitle());
        Assert.assertEquals("Invalid request body: \" is missing", problemDetail.getDetail());
        Assert.assertNull(problemDetail.getType());
        Assert.assertNull(problemDetail.getErrors());

        verify(this.httpServletRequest, times(1)).getRequestURI();
        verifyNoMoreInteractions(this.httpServletRequest);
    }

    @Test
    public void testHandleMethodArgumentNotValidException() throws Exception {
        when(this.httpServletRequest.getRequestURI()).thenReturn("/books/1");

        Map<String, String> bindingMap = new HashMap<>();
        bindingMap.put("author", "cannot be null");
        BindingResult bindingResult = new MapBindingResult(bindingMap, "book");
        bindingResult.addError(new FieldError("book", "author", "cannot be null"));

        MethodParameter methodParameter = new MethodParameter(this.getClass().getMethod("testHandleMethodArgumentNotValidException"), 0);

        ProblemDetail problemDetail = this.advice.handleMethodArgumentNotValidException(
                new MethodArgumentNotValidException(methodParameter, bindingResult), this.httpServletRequest).getBody();
        Assert.assertEquals(new Integer(400), problemDetail.getStatus());
        Assert.assertEquals("/books/1", problemDetail.getInstance());
        Assert.assertEquals("Validation failed", problemDetail.getTitle());
        Assert.assertNull(problemDetail.getDetail());
        Assert.assertNull(problemDetail.getType());
        Assert.assertEquals(1, problemDetail.getErrors().size());
        Assert.assertEquals("Invalid Parameter", problemDetail.getErrors().get(0).getTitle());

        verify(this.httpServletRequest, times(1)).getRequestURI();
        verifyNoMoreInteractions(this.httpServletRequest);
    }

    @Test
    public void handleMethodArgumentTypeMismatchException() throws ClassNotFoundException {
        when(this.httpServletRequest.getRequestURI()).thenReturn("/books/1");
        MethodArgumentTypeMismatchException methodArgumentTypeMismatchException = mock(MethodArgumentTypeMismatchException.class);
        when(methodArgumentTypeMismatchException.getName()).thenReturn("name");
        when(methodArgumentTypeMismatchException.getValue()).thenReturn("1");
        MethodParameter methodParameter = mock(MethodParameter.class);
        when(methodParameter.getParameterType()).thenAnswer((Answer<Object>) invocationOnMock -> String.class);
        when(methodArgumentTypeMismatchException.getParameter()).thenReturn(methodParameter);


        ProblemDetail problemDetail = this.advice.handleMethodArgumentTypeMismatchException(methodArgumentTypeMismatchException, this.httpServletRequest).getBody();
        Assert.assertEquals(new Integer(400), problemDetail.getStatus());
        Assert.assertEquals("/books/1", problemDetail.getInstance());
        Assert.assertEquals("Field type mismatch", problemDetail.getTitle());
        Assert.assertNull(problemDetail.getType());
        Assert.assertEquals(1, problemDetail.getErrors().size());
        Assert.assertEquals("Wrong field value format", problemDetail.getErrors().get(0).getTitle());
        Assert.assertEquals("Incorrect value '1' for field 'name'. Expected value type 'java.lang.String'", problemDetail.getErrors().get(0).getDetail());

        verify(this.httpServletRequest, times(1)).getRequestURI();
        verifyNoMoreInteractions(this.httpServletRequest);
    }

    @Test
    public void testHandleConstraintViolationException() throws Exception {
        when(this.httpServletRequest.getRequestURI()).thenReturn("/books/1");

        ProblemDetail problemDetail = this.advice.handleConstraintViolations(
                new ConstraintViolationException("Book already exists"), this.httpServletRequest).getBody();
        Assert.assertEquals(new Integer(409), problemDetail.getStatus());
        Assert.assertEquals("/books/1", problemDetail.getInstance());
        Assert.assertEquals("Constraint Violation", problemDetail.getTitle());
        Assert.assertEquals("Book already exists", problemDetail.getDetail());
        Assert.assertNull(problemDetail.getType());
        Assert.assertNull(problemDetail.getErrors());

        verify(this.httpServletRequest, times(1)).getRequestURI();
        verifyNoMoreInteractions(this.httpServletRequest);
    }

}