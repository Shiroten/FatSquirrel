package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.Pac.PacSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.*;

public interface EntityContext {
    XY getSize();

    void tryMove(MiniSquirrel miniSquirrel, XY xy);

    void tryMove(GoodBeast goodBeast, XY xy);

    void tryMove(BadBeast badBeast, XY xy);

    void tryMove(MasterSquirrel masterSquirrel, XY xy);

    void tryMove(PacSquirrel pacSquirrel, XY xy);

    void implode(Entity e, int impactradius);

    PlayerEntity nearestPlayerEntity(XY pos);

    void killEntity(Entity entity);

    void killAndReplace(Entity entity);

    Entity getEntity(XY xy);

    void spawnMiniSquirrel(XY direction, int energy, MasterSquirrel daddy);

    long getRemainingTime();

    EntityType getEntityType(XY xy);

    int getBEAST_MOVE_TIME_IN_TICKS();

    int getMINI_SQUIRREL_MOVE_TIME_IN_TICKS();

    int getGOODBEAST_VIEW_DISTANCE();

    int getBADBEAST_VIEW_DISTANCE();
}
