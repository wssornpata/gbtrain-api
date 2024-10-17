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
        String errorHeader = "An error occurred";
        String errorMessage = ex.getMessage();
        logger.error(wrapperErrorLogger(errorHeader, errorMessage));
        return new ResponseEntity<Object>(new FailureResponse(errorHeader, errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidEntityAndTypoException.class)
    public ResponseEntity<Object> handleInvalidEntityAndTypoException(InvalidEntityAndTypoException ex) {
        String errorHeader = "InvalidEntityAndTypoException";
        String errorMessage = ex.getMessage().concat(" ").concat(ex.getDetailMessage());
        logger.error(wrapperErrorLogger(errorHeader, errorMessage));
        return new ResponseEntity<Object>(new FailureResponse(errorHeader, errorMessage), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RouteNotFoundException.class)
    public ResponseEntity<Object> handleNoRouteFoundException(RouteNotFoundException ex) {
        String errorHeader = "Route not found";
        String errorMessage = ex.getMessage();
        logger.error(wrapperErrorLogger(errorHeader, errorMessage));
        return new ResponseEntity<Object>(new FailureResponse(errorHeader, errorMessage), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> extractedMessages = extractMessage(ex.getDetailMessageArguments());
        String errorHeader = "Validation error";
        String errorMessage = String.join(", ", extractedMessages);
        logger.error(wrapperErrorLogger(errorHeader, errorMessage));

        return new ResponseEntity<Object>(new FailureResponse(errorHeader, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> extractedMessages = extractMessage(ex.getDetailMessageArguments());
        String errorHeader = "Validation error";
        String errorMessage = String.join(", ", extractedMessages);
        logger.error(wrapperErrorLogger(errorHeader, errorMessage));

        return new ResponseEntity<Object>(new FailureResponse(errorHeader, errorMessage), HttpStatus.BAD_REQUEST);
    }

    private List<String> extractMessage(Object[] message) {

        List<String> errorMessages = Arrays.stream(message)
                .map(Object::toString)
                .collect(Collectors.toList());

        return errorMessages.stream()
                .map(msg -> {
                    int colonIndex = msg.indexOf(":");
                    return colonIndex != -1 ? msg.substring(colonIndex + 1).trim() : msg;
                })
                .collect(Collectors.toList());
    }

    private String wrapperErrorLogger(String errorHeader, String errorMessage) {
        return errorHeader.concat(": ").concat(errorMessage);
    }

}