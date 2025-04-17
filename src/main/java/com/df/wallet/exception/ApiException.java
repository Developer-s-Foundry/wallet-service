package com.df.wallet.exception;

public class ApiException extends RuntimeException{
    private final int statusCode;
    private final String responseCode;

    public ApiException(String message, String responseCode,  int statusCode){
        super(message);
        this.responseCode = responseCode;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

}