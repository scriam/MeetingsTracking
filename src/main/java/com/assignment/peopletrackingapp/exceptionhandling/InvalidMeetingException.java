package com.assignment.peopletrackingapp.exceptionhandling;

public class InvalidMeetingException extends RuntimeException {

    private String field;
    public InvalidMeetingException(String field) {
        super("Invalid field " + field);
    }
}
