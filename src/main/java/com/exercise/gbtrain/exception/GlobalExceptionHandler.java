package com.exercise.gbtrain.exception;

import com.exercise.gbtrain.dto.exception.response.FailureResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(GlobalRuntimeException.class)
    public ResponseEntity<Object> handleGlobalRuntimeException(GlobalRuntimeException ex) {
        String errorMessage = "An error occurred: ".concat(ex.getMessage());
        logger.error(errorMessage);
        return new ResponseEntity<Object>(new FailureResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidEntityAndTypoException.class)
    public ResponseEntity<Object> handleInvalidEntityAndTypoException(InvalidEntityAndTypoException ex) {
        String errorMessage = "InvalidEntityAndTypoException : ".concat(ex.getMessage()).concat(" ").concat(ex.getDetailMessage());
        logger.error(errorMessage);
        return new ResponseEntity<Object>(new FailureResponse(errorMessage), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RouteNotFoundException.class)
    public ResponseEntity<Object> handleNoRouteFoundException(RouteNotFoundException ex) {
        String errorMessage = "Route not found: " + ex.getMessage();
        logger.error(errorMessage);
        return new ResponseEntity<Object>(new FailureResponse(errorMessage), HttpStatus.NOT_FOUND);
    }

}