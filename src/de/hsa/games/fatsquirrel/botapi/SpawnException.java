package de.hsa.games.fatsquirrel.botapi;

/**
 * Is thrown when a bot wants to spawn a minisquirrel that is out of its view or hasn't enough energy
 */
public class SpawnException extends Exception{
    public SpawnException(){super();}
    public SpawnException(String s){ super(s);}
}
