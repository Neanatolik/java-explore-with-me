package ru.practicum.exceptions;

import lombok.Getter;

@Getter
public class BadRequest extends RuntimeException {

    private final String reason;

    public BadRequest(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
