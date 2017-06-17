package de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini.ExCells26ReaperMini;

import java.util.Hashtable;

/**
 * Created by Shiroten on 15.06.2017.
 */
public class Cell {

    private XY quadrant;
    private long lastFeedback;
    private ExCells26ReaperMini miniSquirrel;

    public boolean isActive() {
        return isActive;
    }

    private boolean isActive = false;

    public Cell getNextCell() {
        return nextCell;
    }

    public void setNextCell(Cell nextCell) {
        this.nextCell = nextCell;
    }

    private Cell nextCell;
    private Hashtable<XY, Cell> neighbours = new Hashtable<>();

    public Cell(XY position) {
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

    public Cell getConnectingCell() {
        for (Cell c : neighbours.values()) {
            if (c.isActive()) {
                continue;
            }
            if (c.getNeighbour(nextCell.getQuadrant()) != null) {
                return c;
            }
        }
        return null;
    }

    public void setActive(Cell nextCell) {
        isActive = true;
        this.nextCell = nextCell;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Position: " + quadrant + " is active: " + isActive());
        if (nextCell != null)
            sb.append("\n\tNextCell: " + nextCell.getQuadrant());
        int index = 0;
        for (Cell c : neighbours.values()) {
            index++;
            sb.append("\n\t" + index + ". Neighbour: " + c.getQuadrant() + " is active: " + c.isActive());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cell)) {
            return false;
        }
        return ((Cell) o).getQuadrant().equals(quadrant);
    }
}
