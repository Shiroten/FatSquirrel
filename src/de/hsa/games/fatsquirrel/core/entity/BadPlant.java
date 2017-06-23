package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;
import javafx.scene.paint.Color;

/**
 * Can get eaten by Squirrels, but gives negative points
 */
public class BadPlant extends Entity {
    public static final int START_ENERGY = -100;
    private static final EntityType type = EntityType.BADPLANT;
    public static final Color ENTITYCOLOR = Color.color(0, 0.2353, 0);
    public static final Color ENTITYTEXTCOLOR = Color.color(0, 0.7647, 0);
    public static final String defaultName = "BadPlant";

    public BadPlant(int id, XY coordinate) {
        super(START_ENERGY, id, coordinate, ENTITYCOLOR, ENTITYTEXTCOLOR, defaultName);
    }

    public EntityType getEntityType() {
        return type;
    }

    @Override
    public void updateEnergy(int energyDifference) {
        this.energy += energyDifference;
        if (energy > 0)
            energy = 0;
    }
}
