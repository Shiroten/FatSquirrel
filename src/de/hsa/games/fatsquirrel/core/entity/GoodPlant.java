package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;
import javafx.scene.paint.Color;

/**
 * A entity that can be eaten to raise the score
 */
public class GoodPlant extends Entity {
    public static final int START_ENERGY = 100;
    public static final EntityType type = EntityType.GOODPLANT;
    public static final Color ENTITYCOLOR = Color.color(0, 1, 0);
    public static final Color ENTITYTEXTCOLOR = Color.color(0, 0.2157, 0);
    public static final String defaultName = "GoodPlant";

    public GoodPlant(int id, XY coordinate) {
        super(START_ENERGY, id, coordinate, ENTITYCOLOR, ENTITYTEXTCOLOR, defaultName);
    }

    public EntityType getEntityType() {
        return type;
    }

}
