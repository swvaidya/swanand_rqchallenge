package com.example.rqchallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class OperationFailedException extends RuntimeException{

    private static final long serialVersionUID = 1L;

	public OperationFailedException(String message) {
        super(message);
    }

}
