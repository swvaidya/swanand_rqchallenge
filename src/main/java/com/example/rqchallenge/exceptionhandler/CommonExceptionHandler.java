package com.example.rqchallenge.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import com.example.rqchallenge.dto.ErrorResponse;
import com.example.rqchallenge.exception.InvalidRequestException;
import com.example.rqchallenge.exception.OperationFailedException;
import com.example.rqchallenge.exception.ResourceNotFoundException;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleHttpErrorException(HttpClientErrorException ex) {
    	HttpStatus hst = HttpStatus.valueOf(ex.getStatusCode().value());
        ErrorResponse errorDetails = new ErrorResponse(
        		hst.getReasonPhrase(),
        		ex.getStatusCode().value(),
        		hst.getReasonPhrase());
        return new ResponseEntity<>(errorDetails, ex.getStatusCode());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorDetails = new ErrorResponse(
        		ex.getMessage(),
        		HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleInvalidRequestException(InvalidRequestException ex) {
        ErrorResponse errorDetails = new ErrorResponse(
        		ex.getMessage(),
        		HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OperationFailedException.class)
    public ResponseEntity<?> handleOpFailedException(OperationFailedException ex) {
        ErrorResponse errorDetails = new ErrorResponse(ex.getMessage(),
        		HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // TODO: Generic exception handler overriding specific handlers. Check why
    
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleGenericException(Exception ex) {
//    	ErrorResponse errorDetails = new ErrorResponse(
//    			MessageConstants.GENERAL_ERROR_MESSAGE,
//    			HttpStatus.INTERNAL_SERVER_ERROR.value(),
//    			HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//    	return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
