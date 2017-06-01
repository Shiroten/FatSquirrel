package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

/**
 * The parent class of all MiniSquirrels
 * Extends PlayerEntity
 */
public abstract class MiniSquirrel extends PlayerEntity {
    private static final EntityType type = EntityType.MINISQUIRREL;
    private MasterSquirrel daddy;
    int moveCounter = 0;
    int implosionRadius = 5;
    boolean implode = false;

    public EntityType getEntityType() {
        return type;
    }


    public MiniSquirrel(int id, XY coordinate, int startEnergy, MasterSquirrel daddy) {
        super(startEnergy, id, coordinate);
        this.daddy = daddy;

        this.setEntityName(getName(daddy.getFactory()));

    }

    public MasterSquirrel getDaddy() {
        return daddy;
    }

    public String toString() {
        return (super.toString() + "ParentID:" + daddy.getId());
    }

    @Override
    public void nextStep(EntityContext context) {}

    /**
     * Set the flag to implode in the next nextStep()
     * @param implosionRadius How far the implosion should spread. Between 1 and 10.
     */
    public void implode(int implosionRadius) {
        this.implode = true;
        this.implosionRadius = implosionRadius;
    }
}
