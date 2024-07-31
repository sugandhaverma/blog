package org.scaler.my_blog.exception;

import org.scaler.my_blog.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionhandler {
    //handle exception and global exception
@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails>handleResourceNotFound(ResourceNotFoundException exception,
                                                              WebRequest request){

    ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
            request.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    //global exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails>handleGlobalException(Exception exception,
                                                             WebRequest request){

    ErrorDetails errorDetails = new ErrorDetails(new Date(),exception.getMessage(),
            request.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
