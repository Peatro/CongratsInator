package com.peatroxd.congratsinator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotFoundExceptionMessage {
    PERSON_NOT_FOUND("Person not found: ");

    private final String message;
}
