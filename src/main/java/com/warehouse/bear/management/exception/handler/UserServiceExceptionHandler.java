package com.warehouse.bear.management.exception.handler;

import com.warehouse.bear.management.exception.RoleNotFoundException;
import com.warehouse.bear.management.exception.ObjectNotFoundException;
import com.warehouse.bear.management.response.WarehouseExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class UserServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errors = fieldErrors
                .stream()
                .map(err -> err.getField() + ":" + err.getDefaultMessage())
                .collect(Collectors.toList());

        WarehouseExceptionResponse apiError = new WarehouseExceptionResponse();
        apiError.setErrors(errors);
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setPath(request.getDescription(false));
        apiError.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ObjectNotFoundException.class, RoleNotFoundException.class})
    ResponseEntity<?> UserNotFoundHandler(Exception exception, ServletWebRequest request) {

        WarehouseExceptionResponse apiError = new WarehouseExceptionResponse();

        apiError.setTimestamp(LocalDateTime.now());
        apiError.setStatus(HttpStatus.NOT_FOUND);
        apiError.setErrors(Arrays.asList(exception.getMessage()));
        apiError.setPath(request.getDescription(true));

        return new ResponseEntity(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
