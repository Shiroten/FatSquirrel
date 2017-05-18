package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.XY;

public abstract class PlayerEntity extends Character {
    protected int stunTime = 0;

    //Package Private
    PlayerEntity(){
        super();
    }
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
}
