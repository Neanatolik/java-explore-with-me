package ru.practicum.main_service.exceptions;

import lombok.Getter;

@Getter
public class Conflict extends RuntimeException {

    private final String reason;

    public Conflict(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
