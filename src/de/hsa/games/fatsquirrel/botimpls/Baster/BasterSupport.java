package de.hsa.games.fatsquirrel.botimpls.Baster;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.core.entity.BadPlant;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.GoodPlant;
import de.hsa.games.fatsquirrel.core.entity.GoodBeast;

/**
 * Created by tillm on 27.05.2017.
 */
public class BasterSupport {

    public XY preferredDirection(ControllerContext view){
        int[] ratings = rateMyView(view);
        int bestRating = 0;
        for(int i = 0; i <ratings.length; i++){
            if(ratings[i] > ratings[bestRating])
                bestRating = i;
        }
        switch (bestRating){
            case 0: return XY.UP;
            case 1: return XY.RIGHT_UP;
            case 2: return XY.RIGHT;
            case 3: return XY.RIGHT_DOWN;
            case 4: return XY.DOWN;
            case 5: return XY.LEFT_DOWN;
            case 6: return XY.LEFT;
            case 7: return XY.LEFT_UP;
            default: return XY.ZERO_ZERO;
        }
    }

    public int[] rateMyView(ControllerContext view) {
        XY myPosition = view.locate();
        //Speichert die Ratings in folgender Reihenfolge: UP[0], UpperRight[1], Right[2], LowerRight[3], Down[4], LowerLeft[5], Left[6], UpperLeft[7]
        int[] ratings = new int[8];

        //TODO: Mauern mit einberechnen
        for (int i = 1; i < 15; i++) {
            for (int j = 1 - i; j < i; j++) {
                ratings[0] = ratings[0] + ratingOfPosition(view, new XY(myPosition.getX() + j, myPosition.getY() - i));
                ratings[2] = ratings[2] + ratingOfPosition(view, new XY(myPosition.getX() + i, myPosition.getY() + j));
                ratings[4] = ratings[4] + ratingOfPosition(view, new XY(myPosition.getX() + j, myPosition.getY() + i));
                ratings[6] = ratings[6] + ratingOfPosition(view, new XY(myPosition.getX() - i, myPosition.getY() + j));
            }
            for (int j = 1; j < 15; j++){
                ratings[1] = ratings[1] + ratingOfPosition(view, new XY(myPosition.getX() + j, myPosition.getY() -i));
                ratings[3] = ratings[3] + ratingOfPosition(view, new XY(myPosition.getX() + j, myPosition.getY() +i));
                ratings[5] = ratings[5] + ratingOfPosition(view, new XY(myPosition.getX() - j, myPosition.getY() +i));
                ratings[6] = ratings[6] + ratingOfPosition(view, new XY(myPosition.getX() - j, myPosition.getY() -i));
            }
        }

        return ratings;
    }

    private int ratingOfPosition(ControllerContext view, XY position) {
        try {
            EntityType entityType = view.getEntityAt(position);
            switch (entityType) {
                case GOODBEAST:
                    return (GoodBeast.START_ENERGY / (int) position.distanceFrom(view.locate()));
                case GOODPLANT:
                    return (GoodPlant.START_ENERGY / (int) position.distanceFrom(view.locate()));
                case BADBEAST:
                    return - (view.getEnergy() / (int) position.distanceFrom(view.locate()));
                case BADPLANT:
                    return (BadPlant.START_ENERGY / (int) position.distanceFrom(view.locate()));
                case MASTERSQUIRREL:
                    return 0;
                case MINISQUIRREL:
                    if (!view.isMine(position)){
                        if(view.getEntityAt(view.locate()) == EntityType.MASTERSQUIRREL)
                            return 200;
                        else
                            return - (view.getEnergy() / (int) position.distanceFrom(view.locate()));
                    }

            }
        } catch (OutOfViewException outofViewException) {
            return 0;
        }

        return 0;
    }
}
