package de.hsa.games.fatsquirrel.core;


import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.entity.*;
import de.hsa.games.fatsquirrel.core.entity.squirrels.*;
import de.hsa.games.fatsquirrel.core.entity.Character;
import de.hsa.games.fatsquirrel.gui.ImplosionContext;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements all the game rules concerning movement and other actions
 */
public class FlattenedBoard implements BoardView, EntityContext {
    private final XY size;
    private final Board board;

    //Ganz, ganz wichtig für Konsistenz:
    //Ein Mehrdimensionales Array zählt folgendermaßen:
    //a[0][0] a[0][1] a[0][2]
    //a[1][0] ...
    //...
    //a[y][x]
    //Das heißt der erste Wert ist die y-Koordinate (Höhe), der zweite Wert ist die x-Koordinate (Breite)
    private Entity[][] flattenedBoard;

    //Package private per default
    FlattenedBoard(XY size, Board board, Entity[][] flat) {
        this.size = size;
        flattenedBoard = new Entity[size.getY()][size.getX()];
        this.board = board;
        this.flattenedBoard = flat;
    }

    public XY getSize() {

        //Hier wird die Koordinate etwas missbraucht: man speichert in einer Koordinate die x-Höhe
        //und die y-Höhe. Felder sind wohl nicht immer quadratisch...
        return size;
    }

    /**
     *
     * @param xy The position on the field
     * @return EntityType in the given Field
     */
    @Override
    public EntityType getEntityType(XY xy) {
        try {
            return flattenedBoard[xy.getY()][xy.getX()].getEntityType();
        } catch (Exception e) {
            return EntityType.NONE;
        }
    }

    /**
     *
     * @param xy A field on the board
     * @return Entity on the given Field
     */
    @Override
    public Entity getEntity(XY xy) {
        try {
            return flattenedBoard[xy.getY()][xy.getX()];
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getRemainingTime() {
        return board.getRemainingGameTime();
    }

    @Override
    public int getBEAST_MOVE_TIME_IN_TICKS() {
        return board.getConfig().getBeastMoveTimeInTicks();
    }

    @Override
    public int getMINI_SQUIRREL_MOVE_TIME_IN_TICKS() {
        return board.getConfig().getMiniSquirrelMoveTimeInTicks();
    }

    @Override
    public int getGOODBEAST_VIEW_DISTANCE() {
        return board.getConfig().getViewDistanceOfGoodbeast();
    }

    @Override
    public int getBADBEAST_VIEW_DISTANCE() {
        return board.getConfig().getViewDistanceOfBadbeast();
    }

    /**
     *
     * @param en Moves given Entity
     * @param newPosition New Position of the given Entity
     */
    private void move(Entity en, XY newPosition) {
        ((Character) en).setLastDirection(en.getCoordinate().minus(newPosition));

        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINEST,
                String.format("move %s at X: %d, Y: %d to X: %d, Y: %d",
                        en.getEntityType().toString(),
                        en.getCoordinate().getX(),
                        en.getCoordinate().getY(),
                        newPosition.getX(),
                        newPosition.getY()));

        //Alte Position Löschen im Array
        flattenedBoard[en.getCoordinate().getY()][en.getCoordinate().getX()] = null;

        //Neue Position in der EntityList setzen
        en.setCoordinate(newPosition);

        //Neue Position im Array ablegen
        //System.out.println(en.toString());
        flattenedBoard[en.getCoordinate().getY()][en.getCoordinate().getX()] = en;

    }

    /**
     *
     * @param goodBeast The GoodBeast that tries to move
     * @param xy The direction in which the GoodBeast want to move
     */
    @Override
    public void tryMove(GoodBeast goodBeast, XY xy) {
        //Zuerst wird geschaut, auf welchem Feld die Entity landen wird
        //Dann wird geschaut, ob und wenn ja welche Entity sich auf dem Feld befindet
        //In Abhängkeit der E wird die Energie abgezogen
        XY newField = goodBeast.getCoordinate().plus(xy);

        switch (getEntityType(newField)) {
            case WALL:
            case BADBEAST:
            case BADPLANT:
            case GOODBEAST:
            case GOODPLANT:
                break;
            case MINISQUIRREL:
            case MASTERSQUIRREL:
                getEntity(newField).updateEnergy(goodBeast.getEnergy());
                killAndReplace(goodBeast);
                break;
            default:
                move(goodBeast, newField);
        }

    }

    /**
     *
     * @param badBeast The BadBeast that tries to move
     * @param xy The direction in which the BadBeast wants to move
     */
    @Override
    public void tryMove(BadBeast badBeast, XY xy) {

        XY newField = badBeast.getCoordinate().plus(xy);


        switch (getEntityType(newField)) {
            case WALL:
            case BADBEAST:
            case BADPLANT:
            case GOODBEAST:
            case GOODPLANT:
                break;
            case MINISQUIRREL:
                getEntity(newField).updateEnergy(badBeast.getEnergy());
                checkEnergyOfMiniSquirrel((MiniSquirrel) getEntity(newField));

                badBeast.bites();
                if (badBeast.getLives() == 0)
                    killAndReplace(badBeast);
                break;
            case MASTERSQUIRREL:
                getEntity(newField).updateEnergy(badBeast.getEnergy());
                badBeast.bites();
                if (badBeast.getLives() == 0)
                    killAndReplace(badBeast);
                break;
            default:
                move(badBeast, newField);
        }
    }

    /**
     *
     * @param miniSquirrel The squirrel that tries to move
     * @param xy The direction in which the squirrel want to move
     */
    @Override
    public void tryMove(MiniSquirrel miniSquirrel, XY xy) {
        XY newField = miniSquirrel.getCoordinate().plus(xy);

        switch (getEntityType(newField)) {
            case WALL:
                miniSquirrel.updateEnergy(Wall.START_ENERGY);
                miniSquirrel.setStunTime(board.getConfig().getSquirrelStunTimeInTicks());
                checkEnergyOfMiniSquirrel(miniSquirrel);
                break;

            case BADBEAST:
                miniSquirrel.updateEnergy(getEntity(newField).getEnergy());
                checkEnergyOfMiniSquirrel(miniSquirrel);
                ((BadBeast) getEntity(newField)).bites();
                if (((BadBeast) getEntity(newField)).getLives() == 0)
                    killAndReplace(getEntity(newField));
                break;

            case BADPLANT:
            case GOODBEAST:
            case GOODPLANT:
                miniSquirrel.updateEnergy(getEntity(newField).getEnergy());
                killAndReplace(getEntity(newField));
                if (!checkEnergyOfMiniSquirrel(miniSquirrel)){
                    move(miniSquirrel, newField);
                }
                break;

            case MINISQUIRREL:
                if (miniSquirrel.getDaddy() != ((MiniSquirrel) getEntity(newField)).getDaddy()) {
                    killEntity(miniSquirrel);
                    killEntity(getEntity(newField));
                }
                break;

            case MASTERSQUIRREL:

                if (((MasterSquirrel) getEntity(newField)).mySquirrel(miniSquirrel)) {

                    //Kollision mit eigenem MasterSquirrel: auflösen und Energie übertragen
                    (getEntity(newField)).updateEnergy(miniSquirrel.getEnergy());
                    killEntity(miniSquirrel);

                } else {
                    //Kollision mit fremdem MasterSquirrel: nur auflösen
                    killEntity(miniSquirrel);
                }
                break;

            default:
                //Keine Kollisionen: einfacher Move
                checkEnergyOfMiniSquirrel(miniSquirrel);
                move(miniSquirrel, newField);
        }
        miniSquirrel.updateEnergy(-1);
    }

    /**
     *
     * @param mini MiniSquirrel to Check
     * @return true if energy of MiniSquirrel is greater zero and kills the miniSquirrel and returns false if it is still alive
     */
    private boolean checkEnergyOfMiniSquirrel(MiniSquirrel mini){
        if (mini.getEnergy() <= 0){
            killEntity(mini);
            return true;
        } else{
            return false;
        }
    }

    /**
     *
     * @param masterSquirrel The MasterSquirrel that tries to move
     * @param xy The direction in which the MasterSquirrel wants to move
     */
    @Override
    public void tryMove(MasterSquirrel masterSquirrel, XY xy) {

        XY newField = masterSquirrel.getCoordinate().plus(xy);
        switch (getEntityType(newField)) {
            case WALL:
                masterSquirrel.updateEnergy(Wall.START_ENERGY);
                masterSquirrel.setStunTime(board.getConfig().getSquirrelStunTimeInTicks());
                break;
            case BADBEAST:
                masterSquirrel.updateEnergy(getEntity(newField).getEnergy());
                ((BadBeast) getEntity(newField)).bites();
                if (((BadBeast) getEntity(newField)).getLives() == 0)
                    killAndReplace(getEntity(newField));
                break;
            case BADPLANT:
            case GOODBEAST:
            case GOODPLANT:
                moveAndKill(masterSquirrel, getEntity(newField).getEnergy(), newField);
                break;
            case MINISQUIRREL:

                MiniSquirrel squirrel = (MiniSquirrel) getEntity(newField);
                int energy;

                if (masterSquirrel.mySquirrel(squirrel)) {
                    //Eigener MiniSquirrel wird absorbiert
                    energy = squirrel.getEnergy();
                } else {
                    //Fremder MiniSquirrel wird gerammt
                    energy = board.getConfig().getPointsForMiniSquirrel();
                }

                masterSquirrel.updateEnergy(energy);
                killEntity(squirrel);
                move(masterSquirrel, newField);
                break;

            case MASTERSQUIRREL:
                //Stößen nur mit den Köpfen Zusammen
                break;
            default:
                move(masterSquirrel, newField);
        }
    }

    /**
     *
     * @param direction The direction from the master in which the MiniSquirrel should be spawned
     * @param energy The startEnergy of the MiniSquirrel
     * @param daddy The father of the MiniSquirrel
     */
    public void spawnMiniSquirrel(XY direction, int energy, MasterSquirrel daddy) {
        if (getEntityType(daddy.getCoordinate().plus(direction)) == EntityType.NONE) {
            if (energy <= daddy.getEnergy() && energy >= 0) {
                daddy.updateEnergy(-energy);
                board.addMiniSquirrel(direction, energy, daddy);
            }
        }
    }

    /**
     *
     * @param miniSquirrel The MiniSquirrel that implodes
     * @param impactRadius The radius of the implosion. The bigger, the less impact it has on the entities
     */
    @Override
    public void implode(MiniSquirrel miniSquirrel, int impactRadius) {

        int collectedEnergy = 0;
        float impactArea = (float) (impactRadius * impactRadius * Math.PI);
        XY position = miniSquirrel.getCoordinate();

        for (int i = -impactRadius; i <= impactRadius; i++) {
            for (int j = -impactRadius; j <= impactRadius; j++) {

                if ((i == 0 && j == 0))
                    continue;
                if (Math.round(new XY(Math.abs(j), Math.abs(i)).length()) > impactRadius)
                    continue;

                Entity entityToCheck = getEntity(position.plus(new XY(j, i)));

                if (entityToCheck == null)
                    continue;
                if (entityFriendly(miniSquirrel, entityToCheck))
                    continue;

                double distance = position.distanceFrom(entityToCheck.getCoordinate());

                double energyLoss = 200 * (miniSquirrel.getEnergy() / impactArea) * (1 - distance / impactRadius);
                energyLoss = energyLoss < 0 ? 0 : energyLoss;
                collectedEnergy += collectedEnergyOfEntity(energyLoss, entityToCheck);
                EntityType et = entityToCheck.getEntityType();

                switch (et) {
                    case WALL:
                        break;
                    case BADPLANT:
                    case GOODPLANT:
                    case BADBEAST:
                    case GOODBEAST:
                        if (entityToCheck.getEnergy() == 0) {
                            killAndReplace(entityToCheck);
                        }
                        break;
                    case MINISQUIRREL:
                        if (entityToCheck.getEnergy() == 0) {
                            killEntity(entityToCheck);
                        }
                }
            }
        }

        double energyLoss = 200 * (miniSquirrel.getEnergy() / impactArea);
        board.addImplosions(new ImplosionContext((int) energyLoss, impactRadius, position));

        miniSquirrel.getDaddy().updateEnergy(collectedEnergy);
        killEntity(miniSquirrel);

    }

    /**
     *
     * @param e MiniSquirrel which Implode
     * @param toCheck Entity in radius of the Implosion
     * @return if the Entity get the "FreundschaftsPanzer"
     */
    private boolean entityFriendly(Entity e, Entity toCheck) {

        if (e.getEntityType() == EntityType.MINISQUIRREL) {
            MasterSquirrel MasterOfE = ((MiniSquirrel) e).getDaddy();

            switch (toCheck.getEntityType()) {
                case MASTERSQUIRREL:
                    return MasterOfE.equals(toCheck);
                case MINISQUIRREL:
                    return MasterOfE.equals(((MiniSquirrel) toCheck).getDaddy());
                case WALL:
                case NONE:
                    return true;

                default:
                    return false;
            }
        }

        return true;
    }

    /**
     *
     * @param energyLoss amount of Radiation
     * @param e to Check EntityType of given Entity
     * @return collectEnergy
     */
    private int collectedEnergyOfEntity(double energyLoss, Entity e) {

        int energyCollected;
        EntityType et = e.getEntityType();

        switch (et) {
            case BADBEAST:
            case BADPLANT:
                //Fall Energy Negativ
                e.updateEnergy((int) energyLoss);
            case WALL:
                energyCollected = 0;
                break;
            case MASTERSQUIRREL:
                energyCollected = (int) energyLoss;
                e.updateEnergy(-(int) energyLoss);
                break;
            default:
                //Fall Energy Positiv
                energyCollected = (int) energyLoss > e.getEnergy() ? e.getEnergy() : (int) energyLoss;
                e.updateEnergy(-(int) energyLoss);
        }
        return energyCollected;
    }

    /**
     *  Single Method to Move an Entity and spawn a killed one
     * @param en Entity which Moves
     * @param energy Energy Difference to update
     * @param newField New Field to move
     */
    private void moveAndKill(Entity en, int energy, XY newField) {
        en.updateEnergy(energy);
        killAndReplace(getEntity(newField));
        move(en, newField);
    }

    /**
     *
     * @param entity The entity that should be removed
     */
    @Override
    public void killEntity(Entity entity) {
        flattenedBoard[entity.getCoordinate().getY()][entity.getCoordinate().getX()] = null;
        board.killEntity(entity);
    }

    /**
     *
     * @param entity The entity that should be removed
     */
    @Override
    public void killAndReplace(Entity entity) {
        EntityType temp = entity.getEntityType();
        try {
            Entity newE = board.addEntity(temp, randomFreePosition());
            flattenedBoard[newE.getCoordinate().getY()][newE.getCoordinate().getX()] = newE;

            Logger logger = Logger.getLogger(Launcher.class.getName());
            logger.log(Level.FINER,
                    String.format("killAndReplace %s at X: %d, Y: %d to X: %d, Y: %d",
                            newE.getEntityType().toString(),
                            entity.getCoordinate().getX(),
                            entity.getCoordinate().getY(),
                            newE.getCoordinate().getX(),
                            newE.getCoordinate().getY()));
        } catch (FullFieldException e){
            e.printStackTrace();
        }
        killEntity(entity);
    }

    /**
     *
     * @param referencePoint The coordinate that serves as point of reference
     * @return The playerEntity that is closest to the referencePoint
     */
    @Override
    public PlayerEntity nearestPlayerEntity(XY referencePoint) {

        //Aus dem Board alle Player-E's ziehen
        //Abstand von referencePoint zu PEs berechnen
        //nächste PE zurückgeben
        PlayerEntity nearestPlayerEntity = null;
        for (Entity entity : board.getEntityList()) {
            if (entity instanceof PlayerEntity) {
                if (nearestPlayerEntity == null)
                    nearestPlayerEntity = (PlayerEntity) entity;
                else {
                    double distanceToNew = referencePoint.distanceFrom(entity.getCoordinate());
                    if (distanceToNew < referencePoint.distanceFrom(nearestPlayerEntity.getCoordinate()))
                        nearestPlayerEntity = (PlayerEntity) entity;
                }
            }
        }
        return nearestPlayerEntity;
    }

    /**
     *
     * @return A random free position
     * @throws FullFieldException If all fields on the board are occupied
     */
    private XY randomFreePosition() throws FullFieldException{
        XY xy;
        if(board.getEntityList().size() == size.getX() * size.getY())
            throw new FullFieldException();

        do {
            xy = new XY(randomWithRange(1, size.getX() - 1), randomWithRange(1, size.getY() - 1));
        }
        while (flattenedBoard[xy.getY()][xy.getX()] != null);
        return xy;
    }

    public int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }
}
