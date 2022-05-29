package com.instana.exception;

/**
 * @author Simon
 */
public class InputFormatException extends IllegalArgumentException {
    static final long serialVersionUID = -2848938806368998895L;

    public InputFormatException(String s) {
        super(s);
    }

    public InputFormatException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
