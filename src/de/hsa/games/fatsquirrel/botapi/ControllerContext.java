package de.hsa.games.fatsquirrel.botapi;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.console.NotEnoughEnergyException;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

public interface ControllerContext {

    //Todo: 6.1 Proxy zwischenschalten
    XY getViewLowerLeft();
    XY getViewUpperRight();
    XY locate();
    boolean isMine(XY xy) throws OutOfViewException;
    void implode(int impactRadius);
    /**
     *
     * @param xy : cell coordinates
     * @return the type of the entity at that position or none
     * @throws OutOfViewException if xy is outside the view
     */
    EntityType getEntityAt(XY xy) throws OutOfViewException;
    void move(XY direction);
    void spawnMiniBot(XY direction, int energy) throws SpawnException, NotEnoughEnergyException;
    XY directionOfMaster();
    long getRemainingSteps();
    default void shout(String text){};
    int getEnergy();

}
