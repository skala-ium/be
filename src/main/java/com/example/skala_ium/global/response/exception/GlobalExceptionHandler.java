package com.example.skala_ium.global.response.exception;

import com.example.skala_ium.global.response.ApiResponse;
import com.example.skala_ium.global.response.type.ErrorType;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {
        log.warn(e.getMessage(), e);
        return ApiResponse.error(ErrorType.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<?> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.warn(e.getMessage(), e);
        return ApiResponse.error(ErrorType.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ApiResponse<?> handleInvalidDataAccess(InvalidDataAccessApiUsageException e) {
        log.warn(e.getMessage(), e);
        return ApiResponse.error(ErrorType.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        log.warn(ex.getMessage(), ex);

        String errorMessage = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .findFirst()
            .orElse("잘못된 요청입니다.");

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ErrorType.INVALID_REQUEST, errorMessage));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ApiResponse<?> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn(e.getMessage(), e);
        return ApiResponse.error(ErrorType.ENTITY_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    protected ApiResponse<?> handleBusinessException(CustomException e) {
        log.error("CustomException", e);
        return ApiResponse.error(e.getErrorType());
    }
}
