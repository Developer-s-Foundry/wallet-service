package com.df.wallet.exception;

import com.df.wallet.dtos.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException e){
        ApiResponse<?> response = new ApiResponse<>(e.getStatusCode(), e.getMessage(),e.getResponseCode(), false, null);
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getStatusCode()));
    }

}
