package com.assignment.peopletrackingapp.exceptionhandling;

public class InvalidMeetingException extends RuntimeException {

    public InvalidMeetingException(String field) {
        super("Invalid field " + field);
    }
}
