package com.instana.exception;

public class NotFoundException extends Exception {
    static final long serialVersionUID = -2848938806368998896L;
    public NotFoundException(String s){super(s);}
    public NotFoundException(String s, Throwable throwable) { super(s,throwable);}
}
