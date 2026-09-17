package com.anthonyk.Helpdesk.exception;

public class InvalidTicketUpdateException extends RuntimeException {
    public InvalidTicketUpdateException(String message) {
        super(message);
    }
}
