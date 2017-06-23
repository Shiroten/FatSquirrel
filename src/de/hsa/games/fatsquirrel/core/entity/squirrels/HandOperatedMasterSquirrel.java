package de.hsa.games.fatsquirrel.core.entity.squirrels;

import de.hsa.games.fatsquirrel.ActionCommand;
import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botimpls.Player.PlayerFactory;
import de.hsa.games.fatsquirrel.console.NotEnoughEnergyException;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Squirrel the player can control. Gets its commands from the UI.
 * Extends MasterSquirrel
 */
public class HandOperatedMasterSquirrel extends MasterSquirrel {

    private ActionCommand command = ActionCommand.NOWHERE;
    private boolean spawnMiniSquirrel = false;
    private int miniSquirrelSpawnEnergy = 100;

    public void setMiniSquirrelSpawnEnergy(int miniSquirrelSpawnEnergy) {
        this.miniSquirrelSpawnEnergy = miniSquirrelSpawnEnergy;
    }

    public void setCommand(ActionCommand command) {
        this.command = command;
    }

    public HandOperatedMasterSquirrel(int id, XY coordinate) {
        super(id, coordinate);
        setFactory(new PlayerFactory());
        this.setEntityName("Player");
    }

    @Override
    public void nextStep(EntityContext context) {
        if (getStunTime() > 0)
            reduceStunTime();
        else if (getStunTime() == 0 && spawnMiniSquirrel) {
            spawnMiniSquirrel = false;
            try {
                spawnMini(miniSquirrelSpawnEnergy, context);
            } catch (NotEnoughEnergyException e) {
                Logger logger = Logger.getLogger(Launcher.class.getName());
                logger.log(Level.FINEST, "Cant spawn Mini");
            }
        } else {
            switch (command) {
                case SPAWN:
                    spawnMiniSquirrel = true;
                    break;
                case NOWHERE:
                    break;
                default:
                    context.tryMove(this, command.getDirection());
                    break;
            }
            command = ActionCommand.NOWHERE;
        }
    }

    /**
     * Spawn a new MiniSquirrel. Tries to spawn to an empty field next to the MasterSquirrel
     * @param energy The startEnergy of the MiniSquirrel
     * @param context The fields the MasterSquirrel can see
     * @throws NotEnoughEnergyException If the MasterSquirrels energy is greater than the specified energy
     */
    private void spawnMini(int energy, EntityContext context) throws NotEnoughEnergyException {

        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINE, "Spawning Mini");

        XY locationOfMaster = getCoordinate();
        if (getEnergy() >= energy) {
            for (XY xy : XYsupport.directions()) {
                //Wenn dieses Feld leer ist...
                if (context.getEntityType(locationOfMaster.plus(xy)) == EntityType.NONE) {
                    //Füge neues MiniSquirreBot hinzu zum Board
                    context.spawnMiniSquirrel(getCoordinate().plus(xy), energy, this);
                    return;
                }
            }
        } else {
            throw new NotEnoughEnergyException();
        }
    }

}
