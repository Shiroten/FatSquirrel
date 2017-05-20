package de.hsa.games.fatsquirrel.core;


import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.Pac.PacSquirrel;
import de.hsa.games.fatsquirrel.core.entity.Wall;
import de.hsa.games.fatsquirrel.core.entity.GoodPlant;
import de.hsa.games.fatsquirrel.core.entity.BadPlant;
import de.hsa.games.fatsquirrel.core.entity.Entity;
import de.hsa.games.fatsquirrel.core.entity.character.Character;
import de.hsa.games.fatsquirrel.core.entity.character.PlayerEntity;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MiniSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.GoodBeast;
import de.hsa.games.fatsquirrel.core.entity.character.BadBeast;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
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
        return board.getConfig().getGAME_DURATIONE_AT_START();
    }

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
        ((Character) en).setLastVector(en.getCoordinate().minus(newPosition));
        if (en.getEntityType() == EntityType.MINISQUIRREL) {
            //Todo: Entfernen nach dem Finden des MiniSquirrels Bug
            //System.out.println(((Character) en).getLastVector().toString());
        }

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
        //System.out.println(goodBeast.toString() + vector.toString());

        switch (getEntityType(newField)) {
            case WALL:
                //System.out.println("Wallbump");
                break;
            case BADBEAST:
                break;
            case BADPLANT:
                break;
            case GOODBEAST:
                break;
            case GOODPLANT:
                break;
            case MINISQUIRREL:
                getEntity(newField).updateEnergy(goodBeast.getEnergy());
                killAndReplace(goodBeast);
                break;
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

        //System.out.println(badBeast.toString() + vector.toString());

        switch (getEntityType(newField)) {
            case WALL:
                //System.out.println("Running into wall");
                break;
            case BADBEAST:
                break;
            case BADPLANT:
                break;
            case GOODBEAST:
                break;
            case GOODPLANT:
                break;
            case MINISQUIRREL:
                getEntity(newField).updateEnergy(badBeast.getEnergy());
                moveOrKillMiniSquirrel((MiniSquirrel) getEntity(newField), getEntity(newField).getCoordinate());

                badBeast.bites();
                if (badBeast.getLives() == 0)
                    killAndReplace(badBeast);
                break;
            case MASTERSQUIRREL:
                //System.out.println("Squirrel wurde gebissen.%n");
                getEntity(newField).updateEnergy(badBeast.getEnergy());
                badBeast.bites();
                if (badBeast.getLives() == 0)
                    killAndReplace(badBeast);
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
                if (miniSquirrel.getDaddy() !=
                        ((MiniSquirrel) getEntity(newField)).getDaddy()) {

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
                //System.out.println("MS moving");
                move(miniSquirrel, newField);
        }
        miniSquirrel.updateEnergy(-1);
        //TODO: Check is dead
        //moveOrKillMiniSquirrel(miniSquirrel, miniSquirrel.getCoordinate());


    }

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

    public void tryMove(PacSquirrel pacSquirrel, XY xy) {
        XY newField = pacSquirrel.getCoordinate().plus(xy);
        switch (getEntityType(newField)) {
            case WALL:
                break;
            case BADBEAST:
                pacSquirrel.updateEnergy(BadBeast.START_ENERGY);
                ((BadBeast) getEntity(newField)).bites();
                if (((BadBeast) getEntity(newField)).getLives() == 0)
                    killAndReplace(getEntity(newField));
                break;
            case BADPLANT:
                moveAndKill(pacSquirrel, BadPlant.START_ENERGY, newField);
                break;
            case GOODBEAST:
                moveAndKill(pacSquirrel, GoodBeast.START_ENERGY, newField);
                break;
            case GOODPLANT:
                pacSquirrel.updateEnergy(GoodPlant.START_ENERGY);
                killEntity(getEntity(newField));
                break;
            case MINISQUIRREL:

                MiniSquirrel squirrel = (MiniSquirrel) getEntity(newField);
                int energy;

                if (pacSquirrel.mySquirrel(squirrel)) {
                    //Eigener MiniSquirrel wird absorbiert
                    energy = squirrel.getEnergy();
                } else {
                    //Fremder MiniSquirrel wird gerammt
                    energy = board.getConfig().getPOINTS_FOR_MINI_SQUIRREL();
                }

                pacSquirrel.updateEnergy(energy);
                killEntity(squirrel);
                move(pacSquirrel, newField);
                break;

            case MASTERSQUIRREL:
                //Stößen nur mit den Köpfen Zusammen
                break;
            default:
                move(pacSquirrel, newField);
        }
        checkMasterSquirrel(pacSquirrel);
    }

    public void spawnMiniSquirrel(XY direction, int energy, MasterSquirrel daddy) {
        daddy.updateEnergy(-energy);
        board.addMiniSquirrel(direction, energy, daddy);

    }

    @Override
    public void implode(Entity e, int impactRadius) {

        int collectedEnergy = 0;
        float impactArea = (float) (impactRadius * impactRadius * Math.PI);
        XY position = e.getCoordinate();

        for (int i = -impactRadius; i < impactRadius + 1; i++) {
            for (int j = -impactRadius; j < impactRadius + 1; j++) {

                if ((i == 0 && j == 0))
                    continue;
                if (!(Math.round(new XY(Math.abs(j), Math.abs(i)).length()) < impactRadius + 1))
                    continue;

                Entity entityToCheck = getEntity(position.plus(new XY(j, i)));

                if (entityToCheck == null)
                    continue;
                if (entityFriendly(e, entityToCheck))
                    continue;

                double distance = position.distanceFrom(entityToCheck.getCoordinate());

                double energyLoss = 200 * (e.getEnergy() / impactArea) * (1 - distance / impactRadius);
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

        double energyLoss = 200 * (e.getEnergy() / impactArea);
        implosions.add(new ImplosionContext((int) energyLoss, impactRadius, position));

        if (e.getEntityType() == EntityType.MINISQUIRREL) {
            ((MiniSquirrel) e).getDaddy().updateEnergy(collectedEnergy);
            killEntity(e);
        } else {
            //Todo: restliche EntiType behalden für implode falls gewünscht.
        }


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
        for (int i = 0; i < size.getY(); i++) {
            for (int j = 0; j < size.getX(); j++) {
                if (flattenedBoard[i][j] == entity) {
                    flattenedBoard[i][j] = null;
                    board.killEntity(entity);
                    return;
                }
            }
        }
    }

    @Override
    public void killAndReplace(Entity entity) {
        EntityType temp = entity.getEntityType();
        killEntity(entity);
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
    }

    @Override
    public PlayerEntity nearestPlayerEntity(XY pos) {

        //Aus dem Board alle Player-E's ziehen
        //Abstand von pos zu PEs berechnen
        //nächste PE zurückgeben
        PlayerEntity nPE = null;
        for (Entity e : board.getSet().getEntityList()) {
            if (e instanceof PlayerEntity) {
                if (nPE == null)
                    nPE = (PlayerEntity) e;
                else {
                    double distanceToNew = pos.distanceFrom(e.getCoordinate());
                    if (distanceToNew < pos.distanceFrom(nPE.getCoordinate()))
                        nPE = (PlayerEntity) e;
                }
            }
        }
        return nPE;
    }

    private XY randomFreePosition() {
        XY xy;
        do {
            xy = new XY(randomWithRange(1, size.getX() - 1), randomWithRange(1, size.getY() - 1));
            System.out.println("While: " + xy);
        }
        while (flattenedBoard[xy.getY()][xy.getX()] != null);
        System.out.println("Done: " + xy);
        return xy;
    }

    private int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }
}
