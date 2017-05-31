package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GoodBeast extends Character {
    public static final int START_ENERGY = 200;
    public static final EntityType type = EntityType.GOODBEAST;
    public int moveCounter = 0;


    public GoodBeast(int id, XY coordinate) {
        super(START_ENERGY, id, coordinate);
    }

    public EntityType getEntityType() {
        return type;
    }

    public void nextStep(EntityContext context) {

        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINEST, "start nextStep() of GoodBeast");

        if (moveCounter == 0) {
            PlayerEntity pe = context.nearestPlayerEntity(this.getCoordinate());
            XY distance = pe.getCoordinate().minus(this.getCoordinate());

            if (distance.length() < context.getGOODBEAST_VIEW_DISTANCE()) {
                findIdealPath(context, XYsupport.oppositeVector(XYsupport.normalizedVector(distance)), freeFieldMode.goodBeast);
            } else
                findIdealPath(context, XYsupport.randomDirection(), freeFieldMode.goodBeast);
            moveCounter++;
        } else if (moveCounter == context.getBEAST_MOVE_TIME_IN_TICKS())
            moveCounter = 0;
        else
            moveCounter++;
    }
}
