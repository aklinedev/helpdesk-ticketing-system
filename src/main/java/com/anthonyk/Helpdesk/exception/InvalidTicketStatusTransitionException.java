package com.anthonyk.Helpdesk.exception;

public class InvalidTicketStatusTransitionException extends RuntimeException {
    public InvalidTicketStatusTransitionException(String message) {
        super(message);
    }
}
