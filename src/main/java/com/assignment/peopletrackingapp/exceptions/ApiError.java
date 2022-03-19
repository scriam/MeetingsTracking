package com.assignment.peopletrackingapp.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private HttpStatus status;

    private String message;

    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiError() {

    }
}
