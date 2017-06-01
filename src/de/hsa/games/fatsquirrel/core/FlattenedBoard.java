package de.hsa.games.fatsquirrel.core;


import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.entity.*;
import de.hsa.games.fatsquirrel.core.entity.character.*;
import de.hsa.games.fatsquirrel.core.entity.character.Character;
import de.hsa.games.fatsquirrel.gui.ImplosionContext;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlattenedBoard implements BoardView, EntityContext {
    private final XY size;
    private Board board;
    private ArrayList<ImplosionContext> implosions;

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
        this.implosions = board.getImplosions();
    }

    public XY getSize() {

        //Hier wird die Koordinate etwas missbraucht: man speichert in einer Koordinate die x-Höhe
        //und die y-Höhe. Felder sind wohl nicht immer quadratisch...
        return size;
    }

    @Override
    public ArrayList<ImplosionContext> getImplosions() {
        return implosions;
    }

    //TODO: In die UI verlagern, dann aus Board entfernen
    @Override
    public void tickImplosions() {
        ImplosionContext icToDelete = null;
        for (ImplosionContext ic : implosions) {
            ic.updateTick();
            if (ic.getTickCounter() <= 0) {
                icToDelete = ic;
            }
        }
        if (icToDelete != null)
            implosions.remove(icToDelete);
    }

    @Override
    public EntityType getEntityType(XY xy) {
        try {
            return flattenedBoard[xy.getY()][xy.getX()].getEntityType();
        } catch (Exception e) {
            return EntityType.NONE;
        }
    }

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

    //TODO: In Board kapseln
    @Override
    public int getBEAST_MOVE_TIME_IN_TICKS() {
        return board.getConfig().getBEAST_MOVE_TIME_IN_TICKS();
    }

    @Override
    public int getMINI_SQUIRREL_MOVE_TIME_IN_TICKS() {
        return board.getConfig().getMINI_SQUIRREL_MOVE_TIME_IN_TICKS();
    }

    @Override
    public int getGOODBEAST_VIEW_DISTANCE() {
        return board.getConfig().getGOODBEAST_VIEW_DISTANCE();
    }

    @Override
    public int getBADBEAST_VIEW_DISTANCE() {
        return board.getConfig().getVIEW_DISTANCE_OF_BADBEAST();
    }

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

        //Neue Position im EntitySet setzen
        en.setCoordinate(newPosition);

        //Neue Position im Array ablegen
        //System.out.println(en.toString());
        flattenedBoard[en.getCoordinate().getY()][en.getCoordinate().getX()] = en;

    }

    //Zuerst wird geschaut, auf welchem Feld die Entity landen wird
    //Dann wird geschaut, ob und wenn ja welche Entity sich auf dem Feld befindet
    //In Abhängkeit der E wird die Energie abgezogen
    @Override
    public void tryMove(GoodBeast goodBeast, XY xy) {

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
                //TODO: Nicht moveOrKill verwenden
                moveOrKillMiniSquirrel((MiniSquirrel) getEntity(newField), getEntity(newField).getCoordinate());

                badBeast.bites();
                if (badBeast.getLives() == 0)
                    killAndReplace(badBeast);
                break;
            case MASTERSQUIRREL:
                getEntity(newField).updateEnergy(badBeast.getEnergy());
                badBeast.bites();
                if (badBeast.getLives() == 0)
                    killAndReplace(badBeast);
                //TODO: check vielleicht in MasterSquirrel schachteln
                checkMasterSquirrel((MasterSquirrel) getEntity(newField));
                break;
            default:
                move(badBeast, newField);
        }
    }

    @Override
    public void tryMove(MiniSquirrel miniSquirrel, XY xy) {
        XY newField = miniSquirrel.getCoordinate().plus(xy);

        switch (getEntityType(newField)) {
            case WALL:
                miniSquirrel.updateEnergy(Wall.START_ENERGY);
                miniSquirrel.setStunTime(board.getConfig().getSQUIRREL_STUN_TIME_IN_TICKS());
                moveOrKillMiniSquirrel(miniSquirrel, miniSquirrel.getCoordinate());
                break;

            case BADBEAST:
                miniSquirrel.updateEnergy(getEntity(newField).getEnergy());
                moveOrKillMiniSquirrel(miniSquirrel, miniSquirrel.getCoordinate());
                ((BadBeast) getEntity(newField)).bites();
                if (((BadBeast) getEntity(newField)).getLives() == 0)
                    killAndReplace(getEntity(newField));
                break;

            case BADPLANT:
            case GOODBEAST:
            case GOODPLANT:
                moveAndKillMiniSquirrel(miniSquirrel, getEntity(newField).getEnergy(), newField);
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
                move(miniSquirrel, newField);
        }
        miniSquirrel.updateEnergy(-1);
    }

    //TODO: Überarbeiten
    private void moveOrKillMiniSquirrel(MiniSquirrel miniSquirrel, XY newPosition) {

        if (miniSquirrel.getEnergy() <= 0) {
            killEntity(miniSquirrel);
        } else {
            move(miniSquirrel, newPosition);
        }
    }

    private void moveAndKillMiniSquirrel(Entity en, int energy, XY newField) {
        en.updateEnergy(energy);
        killAndReplace(getEntity(newField));
        moveOrKillMiniSquirrel((MiniSquirrel) en, newField);
    }

    @Override
    public void tryMove(MasterSquirrel masterSquirrel, XY xy) {

        XY newField = masterSquirrel.getCoordinate().plus(xy);
        switch (getEntityType(newField)) {
            case WALL:
                masterSquirrel.updateEnergy(Wall.START_ENERGY);
                masterSquirrel.setStunTime(board.getConfig().getSQUIRREL_STUN_TIME_IN_TICKS());
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
                    energy = board.getConfig().getPOINTS_FOR_MINI_SQUIRREL();
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
        checkMasterSquirrel(masterSquirrel);
    }

    public void spawnMiniSquirrel(XY direction, int energy, MasterSquirrel daddy) {
        if (getEntityType(daddy.getCoordinate().plus(direction)) == EntityType.NONE) {
            if (energy <= daddy.getEnergy() && energy >= 0) {
                daddy.updateEnergy(-energy);
                board.addMiniSquirrel(direction, energy, daddy);
            }
        }
    }

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
                collectedEnergy += calculateEnergyOfEntity(energyLoss, entityToCheck);
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
        implosions.add(new ImplosionContext((int) energyLoss, impactRadius, position));

        miniSquirrel.getDaddy().updateEnergy(collectedEnergy);
        killEntity(miniSquirrel);

    }

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

    //TODO: Überarbeiten
    private int calculateEnergyOfEntity(double energyLoss, Entity e) {

        int energyDifference;

        if (e.getEnergy() <= 0) {
            energyDifference = 0;
            ceoeHelper(energyLoss, e);

        } else if (e.getEnergy() > (int) energyLoss) {

            energyDifference = ceoeHelper(energyLoss, e);

        } else if (e.getEntityType() == EntityType.MASTERSQUIRREL) {
            energyDifference = (int) energyLoss;
            e.updateEnergy(-(int) energyLoss);
            checkMasterSquirrel((MasterSquirrel) e);

        } else {

            energyDifference = e.getEnergy();
            e.updateEnergy(-e.getEnergy());

        }
        return energyDifference;
    }

    private int ceoeHelper(double energyLoss, Entity e) {

        int energyCollected;
        EntityType et = e.getEntityType();

        switch (et) {
            case BADBEAST:
            case BADPLANT:
                //Fall Energy Negativ
                e.updateEnergy((int) energyLoss);
                if (e.getEnergy() >= 0) {
                    //Energy auf Null setzen
                    e.updateEnergy(-e.getEnergy());
                }
            case WALL:
                energyCollected = 0;
                break;
            default:
                //Fall Energy Positiv
                energyCollected = (int) energyLoss;
                e.updateEnergy(-(int) energyLoss);
                if (e.getEnergy() <= 0) {
                    e.updateEnergy(-e.getEnergy());
                }
        }
        return energyCollected;
    }

    private void checkMasterSquirrel(MasterSquirrel ms) {
        if (ms.getEnergy() < 0)
            ms.updateEnergy(-ms.getEnergy());
    }

    private void moveAndKill(Entity en, int energy, XY newField) {
        en.updateEnergy(energy);
        killAndReplace(getEntity(newField));
        move(en, newField);
    }

    @Override
    public void killEntity(Entity entity) {
        flattenedBoard[entity.getCoordinate().getY()][entity.getCoordinate().getX()] = null;
        board.killEntity(entity);
    }

    @Override
    public void killAndReplace(Entity entity) {
        EntityType temp = entity.getEntityType();
        Entity newE = board.addEntity(temp, randomFreePosition());
        killEntity(entity);
        flattenedBoard[newE.getCoordinate().getY()][newE.getCoordinate().getX()] = newE;

        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINER,
                String.format("killAndReplace %s at X: %d, Y: %d to X: %d, Y: %d",
                        newE.getEntityType().toString(),
                        entity.getCoordinate().getX(),
                        entity.getCoordinate().getY(),
                        newE.getCoordinate().getX(),
                        newE.getCoordinate().getY()));
    }

    @Override
    public PlayerEntity nearestPlayerEntity(XY referencePoint) {

        //Aus dem Board alle Player-E's ziehen
        //Abstand von referencePoint zu PEs berechnen
        //nächste PE zurückgeben
        PlayerEntity nearestPlayerEntity = null;
        for (Entity entity : board.getSet().getEntityList()) {
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

    //TODO: Volles Board abfangen
    private XY randomFreePosition() {
        XY xy;
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
