package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

public abstract class MasterSquirrel extends PlayerEntity {

    protected BotControllerFactory factory;

    private static final int START_ENERGY = 1000;
    public static final EntityType type = EntityType.MASTERSQUIRREL;
    int moveCounter;

    public EntityType getEntityType() {
        return type;
    }

    public MasterSquirrel(int id, XY coordinate) {
        super(id, coordinate);
        this.energy = START_ENERGY;
    }

    public MasterSquirrel() {
    }

    public void nextStep(EntityContext context) {
        /*
        if (moveCounter == 0) {
            if (stunTime > 0)
                stunTime--;
            else {
                XY distance = XYsupport.randomDirection();
                context.tryMove(this, distance);
            }
            moveCounter++;
        } else if (moveCounter == 1)
            moveCounter = 0;
        else
            moveCounter++;
        */
        if (stunTime > 0)
            stunTime--;
        else {
            XY distance = XYsupport.randomDirection();
            context.tryMove(this, distance);
        }
    }

    public boolean mySquirrel(MiniSquirrel squirrelToCheck) {
        return this == squirrelToCheck.getDaddy();
    }

    public void nextStep(ControllerContext view) {
    }

    public BotControllerFactory getFactory() {
        return factory;
    }
}
