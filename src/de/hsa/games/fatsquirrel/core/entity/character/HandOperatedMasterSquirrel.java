package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.ActionCommand;
import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botimpls.PlayerFactory;
import de.hsa.games.fatsquirrel.console.NotEnoughEnergyException;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HandOperatedMasterSquirrel extends MasterSquirrel {

    public static final EntityType type = EntityType.MASTERSQUIRREL;
    private ActionCommand command = ActionCommand.NOWHERE;
    private boolean spawnMiniSquirrel = false;

    public void setMiniSquirrelSpawnEnergy(int miniSquirrelSpawnEnergy) {
        this.miniSquirrelSpawnEnergy = miniSquirrelSpawnEnergy;
    }

    private  int miniSquirrelSpawnEnergy = 100;

    public HandOperatedMasterSquirrel(int id, XY coordinate) {
        super(id, coordinate);
        this.factory = new PlayerFactory();
        this.setEntityName("Player");
    }

    public void setCommand(ActionCommand command) {
        this.command = command;
    }

    public EntityType getEntityType() {
        return type;
    }

    public void nextStep(EntityContext context){
        if(stunTime > 0)
            stunTime--;
        if(stunTime == 0 && spawnMiniSquirrel){
            spawnMiniSquirrel = false;
            try{
                spawnMini(miniSquirrelSpawnEnergy, context);
            } catch (NotEnoughEnergyException e){

            }
        }

        else {
            switch (command) {
                case EAST:
                    context.tryMove(this, XY.RIGHT);
                    break;
                case WEST:
                    context.tryMove(this, XY.LEFT);
                    break;
                case NORTH:
                    context.tryMove(this, XY.UP);
                    break;
                case SOUTH:
                    context.tryMove(this, XY.DOWN);
                    break;
                case NORTHEAST:
                    context.tryMove(this, XY.RIGHT_UP);
                    break;
                case NORTHWEST:
                    context.tryMove(this, XY.LEFT_UP);
                    break;
                case SOUTHEAST:
                    context.tryMove(this, XY.RIGHT_DOWN);
                    break;
                case SOUTHWEST:
                    context.tryMove(this, XY.LEFT_DOWN);
                    break;
                case SPAWN:
                    spawnMiniSquirrel = true;
                    break;
                case NOWHERE:
                    break;
                default:
                    break;
            }
            command = ActionCommand.NOWHERE;
        }
    }

    private void spawnMini(int energy, EntityContext context) throws NotEnoughEnergyException {

        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINE, "Spawning Mini");

        XY locationOfMaster = getCoordinate();
        for (XY xy : XYsupport.directions()) {
            //Wenn dieses Feld leer ist....
            if (getEnergy() >= energy) {
                if (context.getEntityType(locationOfMaster.plus(xy)) == EntityType.NONE) {

                    //Füge neues MiniSquirreBot hinzu zum Board
                    //updateEnergy(-energy);
                    context.spawnMiniSquirrel(getCoordinate().plus(xy), energy, this);
                    return;
                }
            } else {
                throw new NotEnoughEnergyException();
            }
        }
    }

}
