package com.instana.exception;

/**
 * @author Simon
 */
public class TraceNotFoundException extends NotFoundException {
    static final long serialVersionUID = -2848938806368998897L;

    public TraceNotFoundException(String s) {
        super(s);
    }

    public TraceNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
