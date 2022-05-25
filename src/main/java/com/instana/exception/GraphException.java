package com.instana.exception;


/**
 * Presenting some unexpected errors which happened in graphic calculation
 * Please contact graphic calculation developer when you see it.
 * @author Simon
 */
public class GraphException extends Exception{
    static final long serialVersionUID = -2848938806368998894L;

    public GraphException(String s) {super (s);}
}
