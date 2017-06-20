package de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.BotCom;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.Cell;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.PathFinder;
import de.hsa.games.fatsquirrel.core.FullFieldException;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

/**
 * Created by Shiroten on 15.06.2017.
 */
public class ExCells26ReaperMini implements BotController {

    private BotCom botCom;
    private Cell myCell;

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
        XY toMove = calculateTarget(view);

        PathFinder pf = new PathFinder();
        XY betterMove = XY.ZERO_ZERO;
        try {
            betterMove = pf.directionTo(view.locate(), toMove, view);
        } catch (FullFieldException e) {
            //Todo: add to Log
            //e.printStackTrace();
        }

        if (view.locate().plus(betterMove).equals(botCom.positionOfExCellMaster)) {
            betterMove = XY.ZERO_ZERO;
        }
        view.move(betterMove);


    }

    private XY calculateTarget(ControllerContext view) {
        XY positionOfGoodTarget;
        XY positionOfBadTarget;

        try {
            positionOfBadTarget = getTarget(view, false);
        } catch (NoTargetException e) {
            positionOfBadTarget = new XY(999, 999);
        }

        if (positionOfBadTarget.minus(view.locate()).length() < 2.9) {
            return XYsupport.rotate(XYsupport.Rotation.clockwise,
                    getOppositeVector(view, positionOfBadTarget),
                    1);
        }

        try {
            positionOfGoodTarget = getTarget(view, true);
        } catch (NoTargetException e) {
            positionOfGoodTarget = myCell.getQuadrant();
        }

        return positionOfGoodTarget;

    }

    private XY getOppositeVector(ControllerContext view, XY positionOfBadTarget) {
        XY vectorToBadTarget = XYsupport.oppositeVector(positionOfBadTarget.minus(view.locate()));
        try {
            if (view.getEntityAt(view.locate().plus(vectorToBadTarget)) != EntityType.BADBEAST)
                return view.locate().plus(vectorToBadTarget);
        } catch (OutOfViewException e) {
            XY vector = XYsupport.normalizedVector(vectorToBadTarget);
            return getOppositeVector(view, positionOfBadTarget.plus(vector));
        }
        return view.locate().plus(vectorToBadTarget);
    }

    protected XY getTarget(ControllerContext view, boolean isGood) throws NoTargetException {
        XY positionOfTentativelyTarget = new XY(999, 999);
        for (int j = view.getViewUpperRight().getY(); j < view.getViewLowerLeft().getY(); j++) {
            for (int i = view.getViewLowerLeft().getX(); i < view.getViewUpperRight().getX(); i++) {
                if (isGood) {
                    if (!isInside(new XY(i, j))) {
                        continue;
                    }
                    if (!isGoodTargetAt(view, new XY(i, j))) {
                        continue;
                    }
                } else {
                    if (!isBadTargetAt(view, new XY(i, j))) {
                        continue;
                    }
                }
                if (new XY(i, j).minus(view.locate()).length() < positionOfTentativelyTarget.minus(view.locate()).length()) {
                    positionOfTentativelyTarget = new XY(i, j);
                }
            }
        }
        if (positionOfTentativelyTarget.length() > 100)
            throw new NoTargetException();
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

    private boolean isBadTargetAt(ControllerContext view, XY position) {
        try {
            if (view.getEntityAt(position) == EntityType.BADBEAST ||
                    view.getEntityAt(position) == EntityType.BADPLANT) {
                return true;
            }
            if (view.getEntityAt(position) == EntityType.MASTERSQUIRREL && !view.isMine(position)) {
                return true;
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
            toMove = pf.directionTo(view.locate(), toMove, view);
        } catch (FullFieldException e) {
            toMove = XY.ZERO_ZERO;
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
