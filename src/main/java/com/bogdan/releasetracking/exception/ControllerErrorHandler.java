package com.bogdan.releasetracking.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Objects;

@RestControllerAdvice
public class ControllerErrorHandler extends ResponseEntityExceptionHandler {

    private static final String BAD_REQUEST = "BAD_REQUEST";

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {

        String errorDetails = "Unacceptable JSON " + exception.getMessage();

        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ifx = (InvalidFormatException) exception.getCause();
            if (Objects.nonNull(ifx.getTargetType()) && ifx.getTargetType().isEnum()) {
                errorDetails = String.format("Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.",
                        ifx.getValue(), ifx.getPath().get(ifx.getPath().size()-1).getFieldName(), Arrays.toString(ifx.getTargetType().getEnumConstants()));
            }
        }
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle(BAD_REQUEST);
        errorResponse.setDescription(errorDetails);
        return handleExceptionInternal(exception, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

}