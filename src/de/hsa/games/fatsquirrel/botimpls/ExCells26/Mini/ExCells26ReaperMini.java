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
import de.hsa.games.fatsquirrel.core.entity.EntityType;

import java.util.ArrayList;

public class ExCells26ReaperMini implements BotController {

    final BotCom botCom;
    private Cell myCell;

    final ArrayList<XY> unReachableGoodies = new ArrayList<>();
    private XY cornerVector = XY.UP;
    boolean goToMaster = false;

    public ExCells26ReaperMini(BotCom botCom) {
        this.botCom = botCom;
        this.myCell = botCom.getCellForNextMini();
        this.myCell.setMiniSquirrel(this);
    }

    @Override
    public void nextStep(ControllerContext view) {
        if (view.getRemainingSteps() < 200) {
            goToMaster = true;
        }

        if (goToMaster) {
            executeGoToMaster(view);
            return;
        }

        if (view.getEnergy() > 1000 && view.getRemainingSteps() > 1600) {
            goToMaster = true;
        }

        if (view.getEnergy() > 2000) {
            goToMaster = true;
        }


        myCell.setLastFeedback(view.getRemainingSteps());


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
        if (!goToMaster) {
            try {
                if (view.isMine(view.locate().plus(toMove))) {
                    toMove = XY.ZERO_ZERO;
                }
            } catch (OutOfViewException e) {
                //Todo: add log
            }
        }
        view.move(toMove);
    }

    public void setMyCell(Cell myCell) {
        this.myCell = myCell;
    }

    public void setGoToMaster() {
        this.goToMaster = true;
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

    void executeGoToMaster(ControllerContext view) {
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
            //noinspection unused
            @SuppressWarnings("unused") String s = "executeGoToMaster Error";
        }
    }

    private XY getAccuratePositionOfMaster(ControllerContext view) throws NoTargetException {
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

    XY calculateTarget(ControllerContext view) throws NoTargetException {
        unReachableGoodies.clear();
        XY positionOfGoodTarget;
        XY toMove;
        PathFinder pf = new PathFinder(botCom);

        //numberOfTries can be incremented if needed
        int numberOfTries = 10;
        for (int i = 0; i < numberOfTries; i++) {
            positionOfGoodTarget = findNextGoodies(view);
            try {
                toMove = pf.directionTo(view.locate(), positionOfGoodTarget, view);
                return toMove;
            } catch (FullFieldException | FieldUnreachableException e) {
                unReachableGoodies.add(positionOfGoodTarget);
            }
        }
        //Todo: add to Log
        System.out.println("calculateTarget Error");
        throw new NoTargetException();

    }

    private XY findNextGoodies(ControllerContext view) throws NoTargetException {
        XY positionOfTentativelyTarget = new XY(999, 999);
        for (int j = view.getViewUpperLeft().getY(); j < view.getViewLowerRight().getY(); j++) {
            for (int i = view.getViewUpperLeft().getX(); i < view.getViewLowerRight().getX(); i++) {

                if (unReachableGoodies.contains(new XY(i, j))) {
                    continue;
                }

                if (!(this instanceof ExCells26FeralMini)) {
                    if (!myCell.isInside(new XY(i, j), botCom)) {
                        continue;
                    }
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

    private boolean isGoodTargetAt(ControllerContext view, XY position) {
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

}
