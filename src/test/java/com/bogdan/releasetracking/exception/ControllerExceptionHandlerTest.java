package com.bogdan.releasetracking.exception;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ControllerExceptionHandlerTest {

    @InjectMocks
    @Spy
    private ControllerExceptionHandler controllerExceptionHandler;

    @Mock
    private HttpHeaders headers;

    @Mock
    private WebRequest request;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void handleHttpMessageNotReadable_withInvalidEnumValue_returnsBadRequest() {
        String json = "{\"status\":\"INVALID\"}";
        HttpInputMessage inputMessage = new MockHttpInputMessage(json.getBytes());
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Unacceptable JSON", inputMessage);

        ResponseEntity<Object> response = controllerExceptionHandler.handleHttpMessageNotReadable(exception, headers, HttpStatus.BAD_REQUEST, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("BAD_REQUEST", errorResponse.getTitle());
        assertEquals("Invalid enum value: 'INVALID' for the field: 'status'. The value must be one of: [CREATED, IN_PROGRESS, FINISHED].", errorResponse.getDescription());
    }

    @Test
    public void handleHttpMessageNotReadable_withInvalidJson_returnsBadRequest() {
        String json = "{\"status\" INVALID}";
        HttpInputMessage inputMessage = new MockHttpInputMessage(json.getBytes());
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Unacceptable JSON", inputMessage);

        ResponseEntity<Object> response = controllerExceptionHandler.handleHttpMessageNotReadable(exception, headers, HttpStatus.BAD_REQUEST, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("BAD_REQUEST", errorResponse.getTitle());
        assertEquals("Unacceptable JSON JSON parse error: Unexpected character ('I' (code 73)): was expecting double-quote to start field name at [Source: (ByteArrayInputStream); line: 1, column: 10]", errorResponse.getDescription());
    }

    @Test
    public void handleMethodArgumentNotValid_withFieldErrors_returnsBadRequest() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("objectName", "fieldName", "message")));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Object> response = controllerExceptionHandler.handleMethodArgumentNotValid(exception, headers, HttpStatus.BAD_REQUEST, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, List<String>> responseBody = (Map<String, List<String>>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(List.of("message"), responseBody.get("errors"));
    }
}

