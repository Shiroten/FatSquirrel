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

/**
 * Created by Shiroten on 15.06.2017.
 */
public class ExCells26ReaperMini implements BotController {

    private BotCom botCom;
    private Cell myCell;
    protected ArrayList<XY> unReachableGoodies = new ArrayList<>();
    private XY cornerVector = XY.UP;

    public ExCells26ReaperMini(BotCom botCom) {
        this.botCom = botCom;
        this.myCell = botCom.getForNextMini();
        this.myCell.setMiniSquirrel(this);
    }

    @Override
    public void nextStep(ControllerContext view) {
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
                toMove = XYsupport.normalizedVector(myCell.getQuadrant().minus(view.locate()));
            }
        }
        view.move(toMove);
    }

    private XY runningCircle(ControllerContext view) throws NoTargetException {
        PathFinder pf = new PathFinder();
        for (int i = 0; i < 8; i++) {
            if (view.locate().equals(myCell.getQuadrant().plus(cornerVector.times(botCom.getCellsize() / 2)))) {
                cornerVector = XYsupport.rotate(XYsupport.Rotation.clockwise, cornerVector, 1);
            }
            //Todo: remove after Debugging
            System.out.println("CornerVector " + cornerVector.times(botCom.getCellsize() / 2));
            System.out.println("Cell Quadrant: " + myCell.getQuadrant());
            System.out.println(myCell.getQuadrant().plus(cornerVector.times(botCom.getCellsize() / 2)));
            if (pf.isWalkable(myCell.getQuadrant().plus(cornerVector.times(botCom.getCellsize() / 2)))) {
                return XYsupport.normalizedVector(myCell.getQuadrant().plus(cornerVector.times(botCom.getCellsize() / 2)).minus(view.locate()));
            } else {
                cornerVector = XYsupport.rotate(XYsupport.Rotation.clockwise, cornerVector, 1);
            }
        }
        throw new NoTargetException();
    }

    private XY calculateTarget(ControllerContext view) throws NoTargetException {
        unReachableGoodies.clear();
        XY positionOfGoodTarget;
        XY toMove = XY.ZERO_ZERO;
        PathFinder pf = new PathFinder();

        //numberOfTries can be incremented if needed
        int numberOfTries = 10;
        for (int i = 0; i < numberOfTries; i++) {
            positionOfGoodTarget = findNextGoodies(view);

            try {
                toMove = pf.directionTo(view.locate(), positionOfGoodTarget, view, botCom);
            } catch (FullFieldException e) {
                unReachableGoodies.add(positionOfGoodTarget);
            } catch (FieldUnreachableException e) {
                unReachableGoodies.add(positionOfGoodTarget);
            }

            if (toMove.equals(XY.ZERO_ZERO)) {
                unReachableGoodies.add(positionOfGoodTarget);
            } else {
                return toMove;
            }
        }
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

                if (!isInside(new XY(i, j))) {
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

    private void endOfSeason(ControllerContext view) {
        XY toMove = botCom.positionOfExCellMaster;
        PathFinder pf = new PathFinder();
        try {
            toMove = pf.directionTo(view.locate(), toMove, view, botCom);
        } catch (FullFieldException e) {
            toMove = XY.ZERO_ZERO;
        } catch (FieldUnreachableException e) {

        }
        view.move(XYsupport.normalizedVector(toMove));
    }

    protected boolean isInside(XY target) {
        /*
        if (Math.abs((myCell.getQuadrant().getX() - target.getX())) > 10) {
            return false;
        }
        if (Math.abs((myCell.getQuadrant().getY() - target.getY())) > 10) {
            return false;
        }
        */
        //Original Version:

        if (Math.abs((myCell.getQuadrant().getX() - target.getX())) > botCom.getCellsize() / 2) {
            return false;
        }
        if (Math.abs((myCell.getQuadrant().getY() - target.getY())) > botCom.getCellsize() / 2) {
            return false;
        }


        return true;
    }

}
