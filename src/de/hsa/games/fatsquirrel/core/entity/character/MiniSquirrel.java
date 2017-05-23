package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MiniSquirrel extends PlayerEntity {
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
        return ("MiniSquirrel: " + super.toString() + "Luke, wer ist dein Vater? ParentID:" + daddy.getId());
    }

    public void nextStep(EntityContext context) {

        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINEST, "start nextStep() of MiniSquirrel");

        if (moveCounter == 0) {
            if (stunTime > 0)
                stunTime--;
            else {
                if (implode) {
                    System.out.println(implosionRadius);
                    context.implode(this, implosionRadius);
                } else {

                    XY distance = XYsupport.randomDirection();
                    context.tryMove(this, distance);
                }
            }
            moveCounter++;
        } else if (moveCounter == context.getMINI_SQUIRREL_MOVE_TIME_IN_TICKS())
            moveCounter = 0;
        else
            moveCounter++;
    }

    public void implode(int implosionRadius) {
        this.implode = true;
        this.implosionRadius = implosionRadius;
    }
}
