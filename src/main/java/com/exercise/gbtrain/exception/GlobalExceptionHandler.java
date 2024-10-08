package com.exercise.gbtrain.exception;

import com.exercise.gbtrain.dto.exception.response.FailureResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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

    @Override
//    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errorMessages = Arrays.stream(ex.getDetailMessageArguments())
                .map(Object::toString)
                .collect(Collectors.toList());

        List<String> extractedMessages = errorMessages.stream()
                .map(msg -> {
                    int colonIndex = msg.indexOf(":");
                    return colonIndex != -1 ? msg.substring(colonIndex + 1).trim() : msg;
                })
                .collect(Collectors.toList());

        String errorMessage = "Validation failed: " + String.join(", ", extractedMessages);

        logger.error(errorMessage);

        return new ResponseEntity<Object>(new FailureResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errorMessages = Arrays.stream(ex.getDetailMessageArguments())
                .map(Object::toString)
                .collect(Collectors.toList());

        List<String> extractedMessages = errorMessages.stream()
                .map(msg -> {
                    int colonIndex = msg.indexOf(":");
                    return colonIndex != -1 ? msg.substring(colonIndex + 1).trim() : msg;
                })
                .collect(Collectors.toList());

        String errorMessage = "Validation failed: " + String.join(", ", extractedMessages);

        logger.error(errorMessage);

        return new ResponseEntity<Object>(new FailureResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }

}