package com.mylibrary.user.rest.assemblers;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

/**
 * Provides powerful mechanism in formatting resources according to HAL format:
 *
 */
@Getter
abstract class AbstractResourceAssembler<D, T extends ResourceSupport> extends ResourceAssemblerSupport<D, T> {

    private RelProvider relProvider;

    private EntityLinks entityLinks;

    /**
     * Creates a new {@link ResourceAssemblerSupport} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
    AbstractResourceAssembler(Class<?> controllerClass, Class<T> resourceType) {
        super(controllerClass, resourceType);
    }

    /**
     * Sets Rel Provider
     *
     * @param relProvider {@link RelProvider}
     */
    @Autowired
    public void setRelProvider(RelProvider relProvider) {
        this.relProvider = relProvider;
    }

    /**
     * Sets entity links.
     *
     * @param entityLinks {@link EntityLinks}
     */
    @Autowired
    public void setEntityLinks(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }
}
