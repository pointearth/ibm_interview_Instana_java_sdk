package com.instana.exception;

/**
 * @author Simon
 */
public enum ErrorCode {
    INPUT_ERROR(-2),
    NOT_EXIST (-1);
    private final int code;
    ErrorCode( int code) {
        this.code = code;
    }
    public int getValue() {
        return code;
    }
}
