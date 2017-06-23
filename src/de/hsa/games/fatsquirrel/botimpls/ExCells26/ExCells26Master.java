package de.hsa.games.fatsquirrel.botimpls.ExCells26;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.botapi.SpawnException;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.*;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini.ExCells26ReaperMini;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini.MiniType;
import de.hsa.games.fatsquirrel.core.FullFieldException;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExCells26Master implements BotController {

    private final BotCom botCom;
    private Cell currentCell;
    private boolean firstCall = true;
    private ControllerContext view;
    private boolean firstTimeInCell = true;
    private int waitCycleForFeral = 5;

    public ExCells26Master(BotCom botCom) {
        this.botCom = botCom;
    }

    @Override
    public void nextStep(ControllerContext view) {
        toDoAtStartOfNextStep(view);
        if (firstCall) {
            initOfMaster(view);
        }


        if (view.getEnergy() > 500) {
            spawnMoreMinis();
        }


        if (view.getRemainingSteps() < 100) {
            collectingReapers();
            return;
        }

        if (currentCell.isInside(view.locate(), botCom) && firstTimeInCell) {
            collectMiniOfCell();
            return;
        }

        if (currentCell.getQuadrant().equals(view.locate()) && !firstTimeInCell) {
            try {
                botCom.expand();
                if (view.getEnergy() >= 100) {
                    if (currentCell.getMiniSquirrel() == null) {
                        spawningReaper(currentCell);
                    }
                } else {
                    //maybe something better
                    changeCurrentCell();
                }
            } catch (NoConnectingNeighbourException e) {
                //Todo: add to Log
                //e.printStackTrace();
            }
            changeCurrentCell();
        }
        moveToCurrentCell();
    }

    private void spawnMoreMinis() {
        Cell start = currentCell;
        Cell toCheck = start;
        while (true) {
            toCheck = toCheck.getNextCell();
            if (toCheck.getMiniSquirrel() == null) {
                break;
            } else {
                toCheck = toCheck.getNextCell();
            }

            if (toCheck.equals(start)) {
                try {
                    botCom.expand();
                } catch (NoConnectingNeighbourException e) {
                    return;
                }
            }
        }
        spawningReaper(toCheck);
        changeCurrentCell();
    }

    private void collectMiniOfCell() {
        firstTimeInCell = false;
        if (currentCell.getMiniSquirrel() != null) {
            ExCells26ReaperMini miniOfCurrentCell = currentCell.getMiniSquirrel();
            miniOfCurrentCell.setMyCell(null);
            miniOfCurrentCell.setGoToMaster();
            //currentCell.setMiniSquirrel(null);
        }
    }

    private void changeCurrentCell() {
        firstTimeInCell = true;
        currentCell = currentCell.getNextCell();
        //Todo: remove after debugging
        //System.out.println("\nGo to nextCell: " + currentCell);
    }

    private void toDoAtStartOfNextStep(ControllerContext view) {
        botCom.positionOfExCellMaster = view.locate();
        botCom.checkAttendance(view.getRemainingSteps());
        this.view = view;
    }

    private void spawningReaper(Cell cellForMini) {
        try {
            botCom.setNextMiniTypeToSpawn(MiniType.REAPER);
            botCom.setCellForNextMini(cellForMini);
            XY spawnDirection = cellForMini.getNextCell().getQuadrant().minus(view.locate());
            spawnDirection = XYsupport.normalizedVector(spawnDirection).times(-1);
            if (view.getEntityAt(view.locate().plus(spawnDirection)) == EntityType.NONE) {
                view.spawnMiniBot(spawnDirection, 100);
            } else {
                //Todo: adding can't spawn
            }
        } catch (SpawnException | OutOfViewException e) {
            Logger logger = Logger.getLogger(Launcher.class.getName());
            logger.log(Level.FINE, e.getMessage());
        }
    }

    private void moveToCurrentCell() {
        PathFinder pf = new PathFinder(botCom);
        XY betterMove = XY.ZERO_ZERO;
        try {
            betterMove = pf.directionTo(view.locate(), currentCell.getQuadrant(), view);
        } catch (FullFieldException | FieldUnreachableException e) {
            changeCurrentCell();
        }
        view.move(betterMove);
    }

    private void collectingReapers() {
        XY middle = new XY(botCom.getFieldLimit().getX() / 2, botCom.getFieldLimit().getX() / 2);
        PathFinder pf = new PathFinder(botCom);
        XY toMove = XY.ZERO_ZERO;
        //noinspection TryWithIdenticalCatches
        try {
            toMove = pf.directionTo(view.locate(), middle, view);
        } catch (FullFieldException e) {
            Logger logger = Logger.getLogger(Launcher.class.getName());
            logger.log(Level.FINE, e.getMessage());
        } catch (FieldUnreachableException e) {
            Logger logger = Logger.getLogger(Launcher.class.getName());
            logger.log(Level.FINE, e.getMessage());
        }
        view.move(XYsupport.normalizedVector(toMove));
    }

    private void initOfMaster(ControllerContext view) {
        botCom.setStartPositionOfMaster(view.locate());
        //Todo: set right after implementation of reconMini
        //botCom.setFieldLimit(view.locate());
        botCom.setFieldLimit(new XY(80, 60));
        botCom.setMaster(this);
        botCom.init();
        try {
            botCom.expand();
            botCom.expand();
            botCom.expand();
            botCom.expand();
        } catch (NoConnectingNeighbourException e) {
            //Todo: add to Log
            //e.printStackTrace();
        }
        firstCall = false;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }
}
