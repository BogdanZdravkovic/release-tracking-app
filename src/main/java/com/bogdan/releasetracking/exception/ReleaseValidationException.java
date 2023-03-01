package com.bogdan.releasetracking.exception;


public class ReleaseValidationException extends RuntimeException {

    private final String subject;

    public ReleaseValidationException(String message) {
        super(message);
        this.subject = null;
    }

    public ReleaseValidationException(String message, String subject) {
        super(message);
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }
}
