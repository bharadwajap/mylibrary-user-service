package com.mylibrary.user.rest.assemblers;

import com.mylibrary.user.dto.UserResource;
import com.mylibrary.user.rest.UserController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Allows to construct the response according to API Guidelines and HAL format:
 *
 */
@Component
public class UserResourceAssembler extends AbstractResourceAssembler<UserResource, UserResource> {

    /**
     * Creates a new {@link ResourceAssemblerSupport} using the given controller class and resource type.
     */
    public UserResourceAssembler() {
        super(UserController.class, UserResource.class);
    }

    /**
     * Allows to append HAL metadata, like self link, links to other resources, etc.
     *
     * @param entity {@link UserResource}
     * @return adjusted {@link UserResource}
     */
    public UserResource toResource(UserResource entity) {
        // Add self link
        entity.add(linkTo(methodOn(UserController.class).getUserById(entity.getUserId())).withSelfRel());

        return entity;
    }

}
