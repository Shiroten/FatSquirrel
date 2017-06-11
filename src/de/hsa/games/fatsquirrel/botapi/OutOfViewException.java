package de.hsa.games.fatsquirrel.botapi;

/**
 * Is thrown when a Bot wants to access a field that is out of its view
 */
public class OutOfViewException extends Exception {
    public OutOfViewException(){super();}
    public OutOfViewException(String s){ super(s);}
}
