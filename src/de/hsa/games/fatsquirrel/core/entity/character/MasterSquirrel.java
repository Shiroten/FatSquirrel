package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

/**
 * The parent class of HandOperatedMasterSquirrel and MasterSquirrelBot
 * Extends PlayerEntity
 */
public abstract class MasterSquirrel extends PlayerEntity {

    protected BotControllerFactory factory;

    private static final int START_ENERGY = 1000;
    public static final EntityType type = EntityType.MASTERSQUIRREL;

    public EntityType getEntityType() {
        return type;
    }

    public MasterSquirrel(int id, XY coordinate) {
        super(START_ENERGY, id, coordinate);
    }

    @Override
    public void nextStep(EntityContext context) {}

    /**
     * Check if a MiniSquirrel was spawned by this MasterSquirrel
     * @param squirrelToCheck a MiniSquirrel
     * @return true, if the MasterSquirrel is the parent of the squirrelToCheck; else false
     */
    public boolean mySquirrel(MiniSquirrel squirrelToCheck) {
        return this == squirrelToCheck.getDaddy();
    }

    /**
     * The factory with which the BotControllers are created
     * @return The BotcontrollerFactory of the MasterSquirrel
     */
    public BotControllerFactory getFactory() {
        return factory;
    }
}
