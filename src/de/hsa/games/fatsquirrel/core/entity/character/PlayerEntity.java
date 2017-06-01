package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;

/**
 * The parent of all Squirrels. Implements stunning functionality
 */
public abstract class PlayerEntity extends Character {
    private int stunTime = 0;

    //Package Private
    PlayerEntity(int id, XY coordinate){
        super(id, coordinate);
    }

    public void setStunTime(int stunTime){
        this.stunTime = stunTime;
    }

    public int getStunTime(){
        return this.stunTime;
    }

    /**
     * Reduce stunTime by one
     */
    void reduceStunTime(){
        if(stunTime > 0)
            stunTime--;
    }

    //TODO: Sobald wir unsere Paketstruktur an 10.1 angepasst haben, Ende des String angeben
    String getName(BotControllerFactory factory){
        return factory.getClass().getName().substring(34);
    }
}
