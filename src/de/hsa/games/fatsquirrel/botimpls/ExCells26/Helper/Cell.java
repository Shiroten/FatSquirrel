package de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini.ExCells26ReaperMini;

/**
 * Created by Shiroten on 15.06.2017.
 */
public class Cell {

    private XY quadrant;
    private int lastFeedback;
    private ExCells26ReaperMini miniSquirrel;

    public XY getQuadrant() {
        return quadrant;
    }

    public void setQuadrant(XY quadrant) {
        this.quadrant = quadrant;
    }

    public int getLastFeedback() {
        return lastFeedback;
    }

    public void setLastFeedback(int lastFeedback) {
        this.lastFeedback = lastFeedback;
    }

    public ExCells26ReaperMini getMiniSquirrel() {
        return miniSquirrel;
    }

    public void setMiniSquirrel(ExCells26ReaperMini miniSquirrel) {
        this.miniSquirrel = miniSquirrel;
    }
}
