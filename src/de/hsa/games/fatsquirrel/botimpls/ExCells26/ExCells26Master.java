package de.hsa.games.fatsquirrel.botimpls.ExCells26;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.SpawnException;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.*;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini.MiniType;
import de.hsa.games.fatsquirrel.core.FullFieldException;

public class ExCells26Master implements BotController {

    private BotCom botCom;
    private Cell currentCell;
    private boolean firstCall = true;

    public ExCells26Master(BotCom botCom) {
        this.botCom = botCom;
    }

    @Override
    public void nextStep(ControllerContext view) {
        if (firstCall) {
            initOfMaster(view);
        }
        botCom.positionOfExCellMaster = view.locate();
        botCom.checkAttendance(view.getRemainingSteps());

        if (currentCell.getQuadrant().equals(view.locate())) {

            try {
                if (currentCell.getMiniSquirrel() == null) {
                    botCom.setNextMini(MiniType.REAPER);
                    botCom.setForNextMini(currentCell);
                    XY spawnDirection = currentCell.getNextCell().getQuadrant().minus(view.locate());
                    view.spawnMiniBot(XYsupport.oppositeVector(XYsupport.normalizedVector(spawnDirection)), 100);
                }
            } catch (SpawnException e) {
                //Todo: add to Log
                //e.printStackTrace();
            }

            //Todo: adding Epsilon distance
            currentCell = currentCell.getNextCell();
            //Todo: remove after debugging
            System.out.println("\nGo to nextCell: " + currentCell);
        } else {
            PathFinder pf = new PathFinder();
            XY toMove = currentCell.getQuadrant();
            XY betterMove = XY.ZERO_ZERO;
            try {
                betterMove = pf.directionTo(view.locate(), toMove, view);
            } catch (FullFieldException e) {
                //Todo: add to Log
                // e.printStackTrace();
            }
            if (!betterMove.equals(XY.ZERO_ZERO)) {
                view.move(betterMove);
            } else {
                try {
                    botCom.expand();
                } catch (NoConnectingNeighbourException e) {
                    //Todo: add to Log
                    //e.printStackTrace();
                }
            }
        }
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
