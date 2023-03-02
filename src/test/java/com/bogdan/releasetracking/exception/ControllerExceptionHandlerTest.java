package com.bogdan.releasetracking.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ControllerExceptionHandlerTest {

    @InjectMocks
    @Spy
    private ControllerExceptionHandler controllerExceptionHandler;

    private HttpHeaders headers;

    @Mock
    private WebRequest request;

    @Mock
    private MethodArgumentNotValidException exception;

    @Before
    public void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void handleMethodArgumentNotValid_InvalidEnumValue() {
        InvalidFormatException ifx = new InvalidFormatException(null, "Invalid enum value", null, null);

        when(exception.getCause()).thenReturn(ifx);

        ResponseEntity<Object> responseEntity = controllerExceptionHandler.handleMethodArgumentNotValid(
                exception, headers, HttpStatus.BAD_REQUEST, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals("BAD_REQUEST", errorResponse.getTitle());
        assertTrue(errorResponse.getDescription().contains("Unacceptable JSON"));
    }

    @Test
    public void handleMethodArgumentNotValid_UnacceptableJSON() {
        String errorMessage = "Error message";

        when(exception.getCause()).thenReturn(null);
        when(exception.getMessage()).thenReturn(errorMessage);

        ResponseEntity<Object> responseEntity = controllerExceptionHandler.handleMethodArgumentNotValid(
                exception, headers, HttpStatus.BAD_REQUEST, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals("BAD_REQUEST", errorResponse.getTitle());
        assertEquals("Unacceptable JSON Error message", errorResponse.getDescription());
    }
}

