package de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini.ExCells26ReaperMini;

import java.util.Hashtable;

public class Cell {

    private XY quadrant;
    private long lastFeedback;
    private ExCells26ReaperMini miniSquirrel;
    private boolean isActive = false;

    private Cell nextCell;
    private final Hashtable<XY, Cell> neighbours = new Hashtable<>();

    boolean isActive() {
        return isActive;
    }

    public Cell getNextCell() {
        return nextCell;
    }

    void setNextCell(Cell nextCell) {
        this.nextCell = nextCell;
    }

    public Cell(XY position) {
        this.quadrant = position;
    }

    public XY getQuadrant() {
        return quadrant;
    }

    public void setQuadrant(XY quadrant) {
        this.quadrant = quadrant;
    }

    long getLastFeedback() {
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

    void addNeighbour(Cell toAdd) {
        neighbours.put(toAdd.getQuadrant(), toAdd);
    }

    private Cell getNeighbour(XY position) {
        return neighbours.get(position);
    }

    Cell getConnectingCell() {
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

    public boolean isInside(XY target, BotCom botCom) {
        return Math.abs((this.getQuadrant().getX() - target.getX())) <= botCom.getCellsize() / 2
                && Math.abs((this.getQuadrant().getY() - target.getY())) <= botCom.getCellsize() / 2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Position: ").append(quadrant).append(" is active: ").append(isActive());
        sb.append("\n\tNextCell: ");
        if (nextCell == null){
            sb.append("none");
        }else{
            sb.append(nextCell.getQuadrant());
        }

        int index = 0;
        for (Cell c : neighbours.values()) {
            index++;
            sb.append("\n\t").append(index).append(". Neighbour: ").append(c.getQuadrant()).append(" is active: ").append(c.isActive());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Cell && ((Cell) o).getQuadrant().equals(quadrant);
    }
}
