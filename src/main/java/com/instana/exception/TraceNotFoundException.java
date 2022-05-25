package com.instana.exception;

public class TraceNotFoundException extends RuntimeException{
    static final long serialVersionUID = -2848938806368998897L;
    public TraceNotFoundException (String s) {super(s);}
    public TraceNotFoundException (String s, Throwable throwable) {super(s,throwable);}
}
