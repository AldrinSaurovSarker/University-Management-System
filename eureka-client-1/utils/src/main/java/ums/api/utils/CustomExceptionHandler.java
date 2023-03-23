package ums.api.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        ExceptionPayload payload = new ExceptionPayload(e.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        return new ResponseEntity<>(payload, payload.getHttpStatus());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException e) {
        ExceptionPayload payload = new ExceptionPayload(e.getMessage(), HttpStatus.FOUND, LocalDateTime.now());
        return new ResponseEntity<>(payload, payload.getHttpStatus());
    }

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<Object> handleInvalidStateException(InvalidStateException e) {
        ExceptionPayload payload = new ExceptionPayload(e.getMessage(), HttpStatus.NOT_ACCEPTABLE, LocalDateTime.now());
        return new ResponseEntity<>(payload, payload.getHttpStatus());
    }
}
