package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;

/**
 * Can get eaten by Squirrels, but gives negative points
 */
public class BadPlant extends Entity {
    public static final int START_ENERGY = -100;
    private static final EntityType type = EntityType.BADPLANT;
    public BadPlant(int id, XY coordinate) {
        super(START_ENERGY, id, coordinate);
    }

    public EntityType getEntityType() {
        return type;
    }

    @Override
    public void updateEnergy(int energyDifference){
        this.energy += energyDifference;
        if(energy > 0)
            energy = 0;
    }
}
