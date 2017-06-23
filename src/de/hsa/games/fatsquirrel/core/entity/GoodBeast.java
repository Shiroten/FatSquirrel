package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.core.entity.squirrels.PlayerEntity;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entity that the player can catch to get points. Runs away from squirrels
 * Extends Character
 */
public class GoodBeast extends Character {
    public static final int START_ENERGY = 200;
    private static final EntityType type = EntityType.GOODBEAST;
    public int moveCounter = 0;

    public GoodBeast(int id, XY coordinate) {
        super(START_ENERGY, id, coordinate);
    }

    public EntityType getEntityType() {
        return type;
    }

    public void nextStep(EntityContext context) {

        if (moveCounter == 0) {
            PlayerEntity pe = context.nearestPlayerEntity(this.getCoordinate());
            XY distance = pe.getCoordinate().minus(this.getCoordinate());

            if (distance.length() < context.getGOODBEAST_VIEW_DISTANCE()) {
                context.tryMove(this, XYsupport.normalizedVector(distance).times(-1));
            } else {
                context.tryMove(this, XYsupport.randomDirection());
            }
            moveCounter++;
        } else if (moveCounter == context.getBEAST_MOVE_TIME_IN_TICKS())
            moveCounter = 0;
        else
            moveCounter++;
    }
}
