package de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.BotCom;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.Cell;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.FieldUnreachableException;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.PathFinder;
import de.hsa.games.fatsquirrel.core.FullFieldException;
import de.hsa.games.fatsquirrel.core.entity.Entity;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

import java.util.ArrayList;

/**
 * Created by Shiroten on 15.06.2017.
 */
public class ExCells26ReaperMini implements BotController {

    private BotCom botCom;
    private Cell myCell;

    protected ArrayList<XY> unReachableGoodies = new ArrayList<>();
    private XY cornerVector = XY.UP;
    private boolean goToMaster = false;

    public ExCells26ReaperMini(BotCom botCom) {
        this.botCom = botCom;
        this.myCell = botCom.getCellForNextMini();
        this.myCell.setMiniSquirrel(this);
    }

    @Override
    public void nextStep(ControllerContext view) {
        if (goToMaster) {
            executeGoToMaster(view);
            return;
        }


        if (view.getEnergy() > 1000) {
            goToMaster = true;
        }

        myCell.setLastFeedback(view.getRemainingSteps());

        if (view.getRemainingSteps() < 200) {
            endOfSeason(view);
            return;
        }

        XY toMove = XYsupport.normalizedVector(myCell.getQuadrant().minus(view.locate()));

        try {
            toMove = calculateTarget(view);
        } catch (NoTargetException e) {
            //If no Goodies
            try {
                toMove = runningCircle(view);
            } catch (NoTargetException e1) {
                //Run back to myCell middle
                PathFinder pf = new PathFinder(botCom);
                try {
                    toMove = pf.directionTo(view.locate(), myCell.getQuadrant(), view);
                } catch (FullFieldException | FieldUnreachableException e2) {
                    //Todo: add To Log
                    //e2.printStackTrace();
                }
            }
        }
        view.move(toMove);
    }

    public void setMyCell(Cell myCell) {
        this.myCell = myCell;
    }

    public void setGoToMaster(boolean goToMaster) {
        this.goToMaster = goToMaster;
    }

    private XY runningCircle(ControllerContext view) throws NoTargetException {
        PathFinder pf = new PathFinder(botCom);
        for (int i = 0; i < 8; i++) {
            if (view.locate().equals(myCell.getQuadrant().plus(cornerVector.times(botCom.getCellsize() / 2)))) {
                cornerVector = XYsupport.rotate(XYsupport.Rotation.clockwise, cornerVector, 1);
            }
            if (pf.isWalkable(myCell.getQuadrant().plus(cornerVector.times(botCom.getCellsize() / 2)), view)) {
                try {
                    return pf.directionTo(view.locate(),
                            myCell.getQuadrant().plus(cornerVector.times(botCom.getCellsize() / 2)),
                            view);
                } catch (FullFieldException | FieldUnreachableException e) {
                    cornerVector = XYsupport.rotate(XYsupport.Rotation.clockwise, cornerVector, 1);
                }
            } else {
                cornerVector = XYsupport.rotate(XYsupport.Rotation.clockwise, cornerVector, 1);
            }
        }
        throw new NoTargetException();
    }

    protected void executeGoToMaster(ControllerContext view) {
        XY positionOfMaster;
        if (botCom.positionOfExCellMaster.minus(view.locate()).length() > 10) {
            positionOfMaster = botCom.positionOfExCellMaster;
        } else {
            try {
                positionOfMaster = getAccuratePositionOfMaster(view);
            } catch (NoTargetException e) {
                positionOfMaster = botCom.positionOfExCellMaster;
            }
        }
        PathFinder pf = new PathFinder(botCom);
        try {
            view.move(pf.directionTo(view.locate(), positionOfMaster, view));
        } catch (FullFieldException | FieldUnreachableException e) {
            //Todo: add to Log
            String s = "executeGoToMaster Error";
        }
    }

    protected XY getAccuratePositionOfMaster(ControllerContext view) throws NoTargetException {
        for (int j = view.getViewUpperLeft().getY(); j < view.getViewLowerRight().getY(); j++) {
            for (int i = view.getViewUpperLeft().getX(); i < view.getViewLowerRight().getX(); i++) {
                try {
                    if (view.getEntityAt(new XY(i, j)) != EntityType.MASTERSQUIRREL) {
                        continue;
                    }
                    if (view.isMine(new XY(i, j))) {
                        return new XY(i, j);
                    }
                } catch (OutOfViewException e) {
                    //Todo: add to Log
                    //e.printStackTrace();
                }
            }
        }
        throw new NoTargetException();
    }

    protected XY calculateTarget(ControllerContext view) throws NoTargetException {
        unReachableGoodies.clear();
        XY positionOfGoodTarget;
        XY toMove = XY.ZERO_ZERO;
        PathFinder pf = new PathFinder(botCom);

        //numberOfTries can be incremented if needed
        int numberOfTries = 10;
        for (int i = 0; i < numberOfTries; i++) {
            positionOfGoodTarget = findNextGoodies(view);

            try {
                toMove = pf.directionTo(view.locate(), positionOfGoodTarget, view);
            } catch (FullFieldException e) {
                unReachableGoodies.add(positionOfGoodTarget);
            } catch (FieldUnreachableException e) {
                unReachableGoodies.add(positionOfGoodTarget);
            }
            return toMove;

        }
        //Todo: add to Log
        System.out.println("calculateTarget Error");
        throw new NoTargetException();

    }

    protected XY findNextGoodies(ControllerContext view) throws NoTargetException {
        XY positionOfTentativelyTarget = new XY(999, 999);
        for (int j = view.getViewUpperLeft().getY(); j < view.getViewLowerRight().getY(); j++) {
            for (int i = view.getViewUpperLeft().getX(); i < view.getViewLowerRight().getX(); i++) {

                if (unReachableGoodies.contains(new XY(i, j))) {
                    continue;
                }

                if (!myCell.isInside(new XY(i, j), botCom)) {
                    continue;
                }

                if (!isGoodTargetAt(view, new XY(i, j))) {
                    continue;
                }
                if (new XY(i, j).minus(view.locate()).length() < positionOfTentativelyTarget.minus(view.locate()).length()) {
                    positionOfTentativelyTarget = new XY(i, j);
                }
            }
        }
        if (positionOfTentativelyTarget.length() > 1000) {
            throw new NoTargetException();
        }
        return positionOfTentativelyTarget;
    }

    protected boolean isGoodTargetAt(ControllerContext view, XY position) {
        try {
            if (view.getEntityAt(position) == EntityType.GOODBEAST ||
                    view.getEntityAt(position) == EntityType.GOODPLANT) {
                return true;
            }
            if (view.getEntityAt(position) == EntityType.MINISQUIRREL && view.isMine(position)) {
                return false;
            }
        } catch (OutOfViewException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void endOfSeason(ControllerContext view) {
        XY toMove = botCom.positionOfExCellMaster;
        PathFinder pf = new PathFinder(botCom);
        try {
            toMove = pf.directionTo(view.locate(), toMove, view);
        } catch (FullFieldException | FieldUnreachableException e) {

        }
        view.move(toMove);
    }


}
