package de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.*;
import de.hsa.games.fatsquirrel.core.FullFieldException;

/**
 * Created by Shiroten on 15.06.2017.
 */
public class ExCells26ReconMini implements BotController {

    private BotCom botCom;
    private int rightFieldLimit = 0;
    private int lowerFieldLimit = 0;
    private BotController reaperAI;

    public ExCells26ReconMini(BotCom botCom) {
        this.botCom = botCom;

    }


    @Override
    public void nextStep(ControllerContext view) {
        if(rightFieldLimit == 0 && lowerFieldLimit == 0){
            moveToPoint(view, view.getViewLowerRight());
            checkRightLimit(view);
            checkLowerLimit(view);

        } else if(rightFieldLimit == 0){
            moveToPoint(view, new XY(view.getViewLowerRight().getX(), view.locate().getY()));
            checkRightLimit(view);
        } else if(lowerFieldLimit == 0){
            moveToPoint(view, new XY(view.getViewLowerRight().getY(), view.locate().getX()));
            checkLowerLimit(view);
        } else if(!botCom.isFieldLimitFound()) {
            System.out.println("Ende gefunden, Captain!");
            botCom.setFieldLimit(new XY(rightFieldLimit, lowerFieldLimit));
            botCom.setFieldLimitFound(true);
        } else{
            if(reaperAI == null) {
                initReaperAi();
            }
        }

    }

    private void checkRightLimit(ControllerContext view){
        if(view.getViewLowerRight().getX() < view.locate().getX() + 10){
            rightFieldLimit = view.getViewLowerRight().getX() - 1;
        }
    }

    private void checkLowerLimit(ControllerContext view){
        if(view.getViewLowerRight().getY() < view.locate().getY() + 10){
            lowerFieldLimit = view.getViewLowerRight().getY() - 1;
        }
    }

    private void initReaperAi(){
        try {
            botCom.setCellForNextMini(botCom.freeCell());
            reaperAI = new ExCells26ReaperMini(botCom);
        } catch (FullGridException e){
            try{
                botCom.expand();
                initReaperAi();
            } catch (NoConnectingNeighbourException nE){
                //Do nothing
            }
        }
    }

    private void moveToPoint(ControllerContext view, XY destination){
        PathFinder pathFinder = new PathFinder(botCom);
        try {
            view.move(pathFinder.directionTo(view.locate(), destination, view));
        } catch (FullFieldException e){
            moveToPoint(view, destination.plus(XY.RIGHT_DOWN));
        } catch (FieldUnreachableException e){
            moveToPoint(view, destination.plus(XY.LEFT_UP));
        }
    }



}
