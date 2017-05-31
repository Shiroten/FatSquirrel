package de.hsa.games.fatsquirrel.botapi;

/**
 * Created by tillm on 13.05.2017.
 * Is thrown when a Bot wants to access a field that is out of its view
 */
public class OutOfViewException extends Exception {
    public OutOfViewException(){super();}
    public OutOfViewException(String s){ super(s);}
}
