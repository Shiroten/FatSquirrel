package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;
import javafx.scene.paint.Color;

/**
 * The parent class for all entities that can make an action, i.e. have a nextStep()
 * Extends Entity
 */
public abstract class Character extends Entity {
    private XY lastDirection = XY.ZERO_ZERO;

    protected Character(int energy, int id, XY coordinate, Color color, String name) {
        super(energy, id, coordinate, color, name);
    }

    /**
     *
     * @return The last direction the squirrels was going
     */
    public XY getLastDirection() {
        return lastDirection;
    }

    /**
     *
     * @param lastDirection the last direction the squirrels was going
     */
    public void setLastDirection(XY lastDirection) {
        this.lastDirection = lastDirection;
    }

    public abstract void nextStep(EntityContext context);

}
