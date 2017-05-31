package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;

public class Wall extends Entity {
    public static final int START_ENERGY = -10;
    public static final EntityType type = EntityType.WALL;

    public EntityType getEntityType() {
        return type;
    }

    public Wall(int id, XY coordinate) {
        super(START_ENERGY, id, coordinate);
    }
}
