package com.assignment.peopletrackingapp.exceptionhandling;

import com.assignment.peopletrackingapp.exceptions.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(InvalidMeetingException.class)
    protected ResponseEntity<Object> handleEntityNotFound(InvalidMeetingException ex) {
        return buildResponseEntity(new ApiError(BAD_REQUEST, ex.getMessage()));
    }

    /*
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiValidationError = new ApiError();
        apiValidationError.setStatus(BAD_REQUEST);
        apiValidationError.setMessage("Validation error "+ ex.getBindingResult().getFieldErrors());
        return buildResponseEntity(apiValidationError);
    }*/

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request ";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error + ex));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Database error " + ex.getCause()));
        }
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCause().getMessage()));
    }


    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
