package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The badbeast chases squirrels and bites them, reducing their points
 * Extends Character
 */
public class BadBeast extends Character {
    public static final int START_ENERGY = -150;
    public static final EntityType type = EntityType.BADBEAST;
    public int moveCounter = 0;
    private int lives;

    public BadBeast(int id, XY coordinate) {
        super(START_ENERGY, id, coordinate);
        this.lives = 7;
    }

    @Override
    public EntityType getEntityType(){
        return type;
    }

    /**
     * reduces the lives of the beast by one
     */
    public void bites() {
        lives--;
    }

    /**
     * Lifes left
     * @return current number of lifes
     */
    public int getLives() {
        return this.lives;
    }

    @Override
    public void nextStep(EntityContext context ) {

        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINEST, "start nextStep() of BadBeast");

        if (moveCounter == 0) {
            PlayerEntity pe = context.nearestPlayerEntity(this.getCoordinate());
            XY distance = new XY(pe.getCoordinate(), this.getCoordinate());

            if (distance.length() < context.getBADBEAST_VIEW_DISTANCE()) {
                tryUnStuck(context, XYsupport.normalizedVector(distance), freeFieldMode.badBeast);
            } else
                tryUnStuck(context, XYsupport.randomDirection(), freeFieldMode.badBeast);
            moveCounter++;
        } else if (moveCounter == context.getBEAST_MOVE_TIME_IN_TICKS())
            moveCounter = 0;
        else
            moveCounter++;
    }

}
