package com.peatroxd.congratsinator.util;

import com.peatroxd.congratsinator.controller.impl.PersonPhotoControllerImpl;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PhotoUrlResolver {
    public String resolvePhotoUrl(UUID personId) throws Exception {
        return WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PersonPhotoControllerImpl.class)
                        .getPhoto(personId)
        ).toUri().getPath();
    }
}
