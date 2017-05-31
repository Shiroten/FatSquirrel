package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;


public abstract class MiniSquirrel extends PlayerEntity {
    public static final EntityType type = EntityType.MINISQUIRREL;
    private MasterSquirrel daddy;
    int moveCounter = 0;
    int implosionRadius = 5;
    boolean implode = false;

    public EntityType getEntityType() {
        return type;
    }


    public MiniSquirrel(int id, XY coordinate, int startEnergy, MasterSquirrel daddy) {
        super(id, coordinate);
        this.daddy = daddy;
        this.energy = startEnergy;

        String factoryName = daddy.getFactory().getClass().getSimpleName();
        CharSequence replaceChars = "Factory";
        CharSequence with = "";
        String newName = factoryName.replace(replaceChars,with);
        newName = newName + "Mini";
        this.setEntityName(newName);

    }

    public MasterSquirrel getDaddy() {
        return daddy;
    }

    public String toString() {
        return (super.toString() + "ParentID:" + daddy.getId());
    }

    @Override
    public void nextStep(EntityContext context) {}

    public void implode(int implosionRadius) {
        this.implode = true;
        this.implosionRadius = implosionRadius;
    }
}
