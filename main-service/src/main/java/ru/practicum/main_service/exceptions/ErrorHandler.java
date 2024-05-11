package ru.practicum.main_service.exceptions;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(final NotFoundException e) {
        return new ApiError(ExceptionUtils.getStackTrace(e),
                e.getMessage(),
                e.getReason(),
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(final Conflict e) {
        return new ApiError(ExceptionUtils.getStackTrace(e),
                e.getMessage(),
                e.getReason(),
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final BadRequest e) {
        return new ApiError(ExceptionUtils.getStackTrace(e),
                e.getMessage(),
                e.getReason(),
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final ConstraintViolationException e) {
        return new ApiError(ExceptionUtils.getStackTrace(e),
                e.getMessage(),
                "Отсутсвует значение",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiError =
                new ApiError(ExceptionUtils.getStackTrace(e),
                        e.getMessage(),
                        "Ошибка валидации входящих данных",
                        HttpStatus.BAD_REQUEST.toString(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return handleExceptionInternal(
                e, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError =
                new ApiError(ExceptionUtils.getStackTrace(e),
                        e.getMessage(),
                        "Отсутсвует входящее значение",
                        HttpStatus.BAD_REQUEST.toString(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return handleExceptionInternal(
                e, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }

}
