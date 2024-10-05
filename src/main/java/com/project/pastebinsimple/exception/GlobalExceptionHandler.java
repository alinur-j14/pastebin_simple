package com.project.pastebinsimple.exception;

import com.project.pastebinsimple.feature.util.DateTimeUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.io.IOException;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleEntityExistsException(EntityExistsException e, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails
                .builder()
                .message(e.getMessage())
                .description(request.getDescription(false))
                .timestamp(DateTimeUtil.getCurrentInstant())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails
                .builder()
                .message(e.getMessage())
                .description(request.getDescription(false))
                .timestamp(DateTimeUtil.getCurrentInstant())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails
                .builder()
                .message(e.getMessage())
                .description(request.getDescription(false))
                .timestamp(DateTimeUtil.getCurrentInstant())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException e, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails
                .builder()
                .message(e.getMessage())
                .description(request.getDescription(false))
                .timestamp(DateTimeUtil.getCurrentInstant())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails
                .builder()
                .message(e.getMessage())
                .description(request.getDescription(false))
                .timestamp(DateTimeUtil.getCurrentInstant())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
