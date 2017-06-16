package de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini.ExCells26ReaperMini;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Shiroten on 15.06.2017.
 */
public class Cell {

    private XY quadrant;
    private long lastFeedback;
    private ExCells26ReaperMini miniSquirrel;

    private boolean isActive = false;
    private Cell nextCell;
    private Hashtable<XY, Cell> neighbours = new Hashtable<>();

    public Cell(XY position){
        this.quadrant = position;
    }

    public XY getQuadrant() {
        return quadrant;
    }

    public void setQuadrant(XY quadrant) {
        this.quadrant = quadrant;
    }

    public long getLastFeedback() {
        return lastFeedback;
    }

    public void setLastFeedback(long lastFeedback) {
        this.lastFeedback = lastFeedback;
    }

    public ExCells26ReaperMini getMiniSquirrel() {
        return miniSquirrel;
    }

    public void setMiniSquirrel(ExCells26ReaperMini miniSquirrel) {
        this.miniSquirrel = miniSquirrel;
    }

    public void addNeighbour(Cell toAdd) {
        neighbours.put(toAdd.getQuadrant(), toAdd);
    }

    public Cell getNeighbour(XY position) {
        return neighbours.get(position);
    }

    public void expand(Cell nextCell) {
        isActive = true;

    }
}
