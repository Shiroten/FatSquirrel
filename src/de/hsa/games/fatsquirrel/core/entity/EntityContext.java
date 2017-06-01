package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.entity.character.*;

/**
 * Allows access to information about the board as well as manipulation of the board
 */
public interface EntityContext {
    /**
     *
     * @return The size of the board
     */
    XY getSize();

    /**
     * Try to move a MiniSquirrel and manipulate the board accordingly, i.e. update energies, entities, stunTime etc
     * @param miniSquirrel The squirrel that tries to move
     * @param xy The direction in which the squirrel want to move
     */
    void tryMove(MiniSquirrel miniSquirrel, XY xy);

    /**
     * Try to move a GoodBeast and manipulate the board accordingly, i.e. update energies, entities etc
     * @param goodBeast The GoodBeast that tries to move
     * @param xy The direction in which the GoodBeast want to move
     */
    void tryMove(GoodBeast goodBeast, XY xy);

    /**
     * Try to move a BadBeast and manipulate the board accordingly, i.e. update energies, entities etc
     * @param badBeast The BadBeast that tries to move
     * @param xy The direction in which the BadBeast wants to move
     */
    void tryMove(BadBeast badBeast, XY xy);

    /**
     * Try to move a MasterSquirrel and manipulate the board accordingly, i.e. update energies, entities, stunTime etc
     * @param masterSquirrel The MasterSquirrel that tries to move
     * @param xy The direction in which the MasterSquirrel wants to move
     */
    void tryMove(MasterSquirrel masterSquirrel, XY xy);

    /**
     * Implode a miniSquirrel, damaging neutral and enemy entities in the vicinity
     * @param miniSquirrel The MiniSquirrel that implodes
     * @param impactRadius The radius of the implosion. The bigger, the less impact it has on the entities
     */
    void implode(MiniSquirrel miniSquirrel, int impactRadius);

    /**
     *
     * @param referencePoint The coordinate that serves as point of reference
     * @return The PlayerEntity that is closest to the referencePoint
     */
    PlayerEntity nearestPlayerEntity(XY referencePoint);

    /**
     * Remove the specified entity from the board
     * @param entity The entity that should be removed
     */
    void killEntity(Entity entity);

    /**
     * Removes the specified entity and creates a new one of the same EntityType at a random position
     * @param entity The entity that should be removed
     */
    void killAndReplace(Entity entity);

    /**
     *
     * @param xy A field on the board
     * @return the Entity at the position, null if empty
     */
    Entity getEntity(XY xy);

    /**
     * Spawn a new MiniSquirrel
     * @param direction The direction from the master in which the MiniSquirrel should be spawned
     * @param energy The startEnergy of the MiniSquirrel
     * @param daddy The father of the MiniSquirrel
     */
    void spawnMiniSquirrel(XY direction, int energy, MasterSquirrel daddy);

    /**
     *
     * @return The ticks that remain before the game ends
     */
    long getRemainingTime();

    /**
     *
     * @param xy The position on the field
     * @return The EntityType of the entity on the field, NONE if empty or OutOfBounds
     */
    EntityType getEntityType(XY xy);

    int getBEAST_MOVE_TIME_IN_TICKS();

    int getMINI_SQUIRREL_MOVE_TIME_IN_TICKS();

    int getGOODBEAST_VIEW_DISTANCE();

    int getBADBEAST_VIEW_DISTANCE();
}
