package com.yaroslav.booknetwork.handler;


import com.yaroslav.booknetwork.exception.OperationNotPermittedException;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handlerException(LockedException exp) {

        return ResponseEntity.status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCode.ACCOUNT_LOCKED.getCode())
                        .businessErrorDescription(BusinessErrorCode.ACCOUNT_LOCKED.getDescription())
                        .error(exp.getMessage())
                        .build());

    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handlerException(DisabledException exp) {

        return ResponseEntity.status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCode.ACCOUNT_DISABLED.getCode())
                        .businessErrorDescription(BusinessErrorCode.ACCOUNT_DISABLED.getDescription())
                        .error(exp.getMessage())
                        .build());

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handlerException(BadCredentialsException exp) {

        return ResponseEntity.status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCode.BAD_CREDENTIAL.getCode())
                        .businessErrorDescription(BusinessErrorCode.BAD_CREDENTIAL.getDescription())
                        .error(BusinessErrorCode.BAD_CREDENTIAL.getDescription())
                        .build());

    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handlerException(MessagingException exp) {

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .error(exp.getMessage())
                        .build());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handlerException(MethodArgumentNotValidException exp) {
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors().forEach(objectError -> {
            var errorMessage = objectError.getDefaultMessage();
            errors.add(errorMessage);
        });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                        .validationErrors(errors)
                        .build()
                );

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handlerException(Exception exp) {
        //log the exception
        exp.printStackTrace();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Internal error, contact the admin")
                        .error(exp.getMessage())
                        .build());

    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> handlerException(OperationNotPermittedException exp) {

        return ResponseEntity.status(BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .error(exp.getMessage())
                        .build());

    }
}
