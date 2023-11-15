package com.example.saqayatask.exceptions;

import com.example.saqayatask.responses.ErrorResponse;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DuplicatedRecordEX.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> duplicatedRecordEX(Exception ex){
        ErrorResponse response= ErrorResponse.builder()
                .success(false)
                .dateTime(LocalDateTime.now())
                .details(Arrays.asList(ex.getMessage()))
                .errorMessage(ex.getLocalizedMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    @ExceptionHandler(value = RecordNotFoundEX.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> recordNotFound(Exception ex){
        ErrorResponse response= ErrorResponse.builder()
                .success(false)
                .dateTime(LocalDateTime.now())
                .details(Arrays.asList(ex.getMessage()))
                .errorMessage(ex.getLocalizedMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }
    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> authenticationException(Exception ex){
        ErrorResponse response= ErrorResponse.builder()
                .success(false)
                .dateTime(LocalDateTime.now())
                .details(Arrays.asList(ex.getMessage()))
                .errorMessage(ex.getLocalizedMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> dataIntegrityViolationException(DataIntegrityViolationException ex){

        ErrorResponse response= ErrorResponse.builder()
                .success(false)
                .dateTime(LocalDateTime.now())
                .details(Arrays.asList(ex.getMessage()))
                .errorMessage(ex.getLocalizedMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getDefaultMessage());
        }

        ErrorResponse error = ErrorResponse.builder()
                .success(false)
                .dateTime(LocalDateTime.now())
                .details(errors)
                .errorMessage(ex.getLocalizedMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
