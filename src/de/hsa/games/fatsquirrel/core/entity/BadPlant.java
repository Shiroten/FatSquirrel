package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;

public class BadPlant extends Entity {
    public static final int START_ENERGY = -100;
    public static final EntityType type = EntityType.BADPLANT;
    public BadPlant(int id, XY coordinate) {
        super(START_ENERGY, id, coordinate);
    }

    public EntityType getEntityType() {
        return type;
    }

    public String toString() {
        return ("BadPlant: " + super.toString());
    }
}
