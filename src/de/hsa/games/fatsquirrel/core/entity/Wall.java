package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;
import javafx.scene.paint.Color;

/**
 * Blocks and stuns characters. Can't be destroyed.
 */
public class Wall extends Entity {
    public static final int START_ENERGY = -10;
    private static final EntityType type = EntityType.WALL;
    private static final Color ENTITYCOLOR = Color.color(0.3804, 0.3804, 0.3765);
    private static final Color ENTITYTEXTCOLOR = Color.gray(1);
    private static final String defaultName = "Wall";

    public EntityType getEntityType() {
        return type;
    }

    public Wall(int id, XY coordinate) {
        super(START_ENERGY, id, coordinate, ENTITYCOLOR, ENTITYTEXTCOLOR, defaultName);
    }
}
