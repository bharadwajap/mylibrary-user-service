package com.mylibrary.user.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.core.MethodParameter;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.core.AnnotationRelProvider;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.MediaTypes.HAL_JSON;

/**
 * While running controller tests in standalone mode a lot of beans won't be automatically configured by Spring.
 * Hence, it's our responsibility to define and register them.
 *
 * @author Vadym Lotar
 * @see HttpMessageConverter
 * @see PagedResourcesAssembler
 */
class AbstractHateoasControllerTest {

    final ObjectMapper objectMapper;

    AbstractHateoasControllerTest() {
        this.objectMapper = prepareObjectMapper();
    }

    HttpMessageConverter<?> getHalMessageConverter() {
        MappingJackson2HttpMessageConverter halConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>(halConverter.getSupportedMediaTypes());
        supportedMediaTypes.add(HAL_JSON);
        halConverter.setSupportedMediaTypes(supportedMediaTypes);
        halConverter.setObjectMapper(this.objectMapper);
        return halConverter;
    }

    private ObjectMapper prepareObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        //Switch to ISO-8601 compliant format
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //the application should now always include unannotated fields for each view
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        objectMapper.registerModule(new Jackson2HalModule());
        objectMapper.setHandlerInstantiator(new Jackson2HalModule.HalHandlerInstantiator(new AnnotationRelProvider(), null, null));

        return objectMapper;
    }


    static class PagedResourceAssemblerHandlerMethodArgumentResolver<T> implements HandlerMethodArgumentResolver {

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterType().equals(PagedResourcesAssembler.class);
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            return new PagedResourcesAssembler<T>(new HateoasPageableHandlerMethodArgumentResolver(), null);
        }
    }
}
