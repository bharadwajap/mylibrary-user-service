package com.mylibrary.user.rest;

import com.mylibrary.user.dto.UserResource;
import com.mylibrary.user.dto.ProblemDetail;
import com.mylibrary.user.rest.assemblers.UserResourceAssembler;
import com.mylibrary.user.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Controller class for User API.
 *
 * @author Bharadwaj Adepu
 */
@RestController
@ExposesResourceFor(UserResource.class)
@RequestMapping("/mylibrary/users")
@Api(value = "Users", description = "User API")
public class UserController {

    private final UserService userService;

    private final UserResourceAssembler userResourceAssembler;

    /**
     * Instantiates new instance of {@link UserService}
     *
     * @param userService {@link UserService}
     */
    @Autowired
    public UserController(UserService userService, UserResourceAssembler userResourceAssembler) {
        this.userService = userService;
        this.userResourceAssembler = userResourceAssembler;
    }

    @ApiOperation(
            value = "Retrieve a User resource by identifier",
            notes = "Make a GET request to retrieve a User by a given identifier",
            response = UserResource.class,
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = UserResource.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ProblemDetail.class),
            @ApiResponse(code = 403, message = "This operation is forbidden for this user", response = ProblemDetail.class),
            @ApiResponse(code = 404, message = "User with the given identifier is not found", response = ProblemDetail.class),
            @ApiResponse(code = 500, message = "Unexpected Internal Error", response = ProblemDetail.class)})
    @RequestMapping(value = "/{userid}", method = RequestMethod.GET, produces = HAL_JSON_VALUE)
    public HttpEntity<UserResource> getUserById(@PathVariable(value = "userid") int userId) {

        UserResource user = this.userResourceAssembler.toResource(this.userService.getUserById(userId));

        return ResponseEntity.ok(user);
    }

    @ApiOperation(
            value = "Retrieve all Users",
            notes = "Make a GET request to retrieve a page of users",
            response = PagedResources.class,
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = PagedResources.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ProblemDetail.class),
            @ApiResponse(code = 403, message = "This operation is forbidden for this user", response = ProblemDetail.class),
            @ApiResponse(code = 404, message = "User with the given identifier is not found", response = ProblemDetail.class),
            @ApiResponse(code = 500, message = "Unexpected Internal Error", response = ProblemDetail.class)})
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<PagedResources<UserResource>> getUsers(@PageableDefault(sort = "userName", direction = Sort.Direction.ASC) final Pageable pageable,
                                                                 final PagedResourcesAssembler<UserResource> pagedResourcesAssembler) {

        return ResponseEntity.ok(pagedResourcesAssembler.toResource(this.userService.getUsers(pageable),
                this.userResourceAssembler,
                enhanceSelfLink(pageable, linkTo(ControllerLinkBuilder.methodOn(UserController.class)
                        .getUsers(pageable, pagedResourcesAssembler)).withSelfRel())));
    }

    @ApiOperation(
            value = "Create a User",
            notes = "Make a POST request to register new User",
            response = UserResource.class,
            code = 201,
            httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created", response = UserResource.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ProblemDetail.class),
            @ApiResponse(code = 403, message = "This operation is forbidden for this user", response = ProblemDetail.class),
            @ApiResponse(code = 404, message = "User with the given identifier is not found", response = ProblemDetail.class),
            @ApiResponse(code = 500, message = "Unexpected Internal Error", response = ProblemDetail.class)})
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public HttpEntity<UserResource> createUser(@Validated @RequestBody final UserResource userResource) {

        UserResource user = this.userResourceAssembler.toResource(this.userService.createUser(userResource));

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    private Link enhanceSelfLink(Pageable pageable, Link selfLink) {
        //this version of Spring has a bug, so selfLink must be enhanced with page, size and sort parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(selfLink.expand().getHref());
        new HateoasPageableHandlerMethodArgumentResolver().enhance(builder, null, pageable);
        return new Link(builder.build().toString());
    }

}
