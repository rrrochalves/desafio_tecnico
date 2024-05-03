package com.people.config;

import com.people.exceptions.AddressNotFoundException;
import com.people.exceptions.PeopleNotFoundException;
import com.people.factory.ErrorApiFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandlers {

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorApi> addressNotFoundException(AddressNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorApiFactory.build(exception.getMessage()));
    }

    @ExceptionHandler(PeopleNotFoundException.class)
    public ResponseEntity<ErrorApi> peopleNotFoundException(PeopleNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorApiFactory.build(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName + ": " + errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
