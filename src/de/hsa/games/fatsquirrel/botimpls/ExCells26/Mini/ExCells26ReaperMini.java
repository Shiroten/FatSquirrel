package de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini;

import de.hsa.games.fatsquirrel.XY;
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
        //Todo: implementation of hunting inside the cell
        XY toMove;
        try {
            toMove = getGoodTarget(view);
        } catch (NoGoodTargetException e) {
            //Todo: bug(sitting around with goodTargets)
            toMove = myCell.getQuadrant();
        }
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

    protected XY getGoodTarget(ControllerContext view) throws NoGoodTargetException {
        XY positionOfTentativelyTarget = new XY(999, 999);
        for (int j = view.getViewUpperRight().getY(); j < view.getViewLowerLeft().getY(); j++) {
            for (int i = view.getViewLowerLeft().getX(); i < view.getViewUpperRight().getX(); i++) {
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
        if (positionOfTentativelyTarget.length() > 40)
            throw new NoGoodTargetException();
        //Todo: remove after debugging
        System.out.println(positionOfTentativelyTarget);
        return positionOfTentativelyTarget;
    }


    protected boolean isInside(XY target) {
        if (Math.abs((myCell.getQuadrant().getX() - target.getX())) > 10) {
            return false;
        }
        if (Math.abs((myCell.getQuadrant().getY() - target.getY())) > 10) {
            return false;
        }
        return true;
    }

    private boolean isGoodTargetAt(ControllerContext view, XY position) {
        try {
            if (view.getEntityAt(position) == EntityType.GOODBEAST ||
                    view.getEntityAt(position) == EntityType.GOODPLANT) {
                return true;
            }

        } catch (OutOfViewException e) {
            e.printStackTrace();
        }
        return false;
    }
}
