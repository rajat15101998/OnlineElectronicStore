package com.lcwd.electronic.store.exceptions;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.FileResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //log
    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //handle ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {

        log.info("Resource Not Found Exception caught");
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .success(true)
                .build();

        return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        HashMap<String, Object> response = new HashMap<>();

        allErrors.stream().forEach(objectError -> {
            String message = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();

            response.put(field, message);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadAPIRequestException.class)
    public ResponseEntity<FileResponseMessage> badAPIRequestExceptionHandler(BadAPIRequestException ex) {

        log.info("Bad API Request Exception caught");
        FileResponseMessage fileResponseMessage = FileResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();


        return new ResponseEntity<FileResponseMessage>(fileResponseMessage, HttpStatus.BAD_REQUEST);
    }
}
