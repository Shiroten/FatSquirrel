package de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.ExCells26Master;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini.MiniType;

import java.util.Hashtable;


/**
 * Created by Shiroten on 15.06.2017.
 */
public class BotCom {

    private ExCells26Master master;
    public Hashtable<XY, Cell> grid = new Hashtable<>();

    private MiniType nextMini;

    public Cell getForNextMini() {
        return forNextMini;
    }

    public void setForNextMini(Cell forNextMini) {
        this.forNextMini = forNextMini;
    }

    private Cell forNextMini;
    private XY fieldLimit;
    //Todo: fieldLimit muss durch das MasterSquirrel auf startPositions view gesetzt werden.
    private boolean fieldLimitFound;
    public XY startPositionOfMaster;
    public XY positionOfExCellMaster;

    //Default: 21 (last working number)
    private final int CELLDISTANCE = 5;

    //Default: 11 (last working number)
    private final int CELLCENTEROFFSET = 3;

    //Default: 21 (last working number)
    private int cellsize = 5;

    public BotController getMaster() {
        return master;
    }

    public void setMaster(ExCells26Master master) {
        this.master = master;
    }

    public void setNextMini(MiniType nextMini) {
        this.nextMini = nextMini;
    }

    public XY getFieldLimit() {
        return fieldLimit;
    }

    public void setFieldLimit(XY fieldLimit) {
        this.fieldLimit = fieldLimit;
    }

    public MiniType getNextMini() {
        return nextMini;
    }

    public int getCellsize() {
        return cellsize;
    }

    public void setCellsize(int cellsize) {
        this.cellsize = cellsize;
    }

    public void setStartPositionOfMaster(XY startPositionOfMaster) {
        this.startPositionOfMaster = startPositionOfMaster;
    }

    public void init() {
        getAllCells();
        Cell firstCell = grid.get(cellAt(startPositionOfMaster));
        master.setCurrentCell(firstCell);

        firstCell.setActive(firstCell);

        for (Cell c : grid.values()) {
            System.out.println(c);
        }
    }

    public void getAllCells() {
        int xLimit = (fieldLimit.getX() - 1) / CELLDISTANCE;
        int yLimit = (fieldLimit.getY() - 1) / CELLDISTANCE;

        for (int i = 0; i <= xLimit; i++) {
            for (int j = 0; j <= yLimit; j++) {
                Cell newCell = new Cell(new XY(CELLCENTEROFFSET + CELLDISTANCE * i, CELLCENTEROFFSET + CELLDISTANCE * j));
                if (!validCell(newCell.getQuadrant())){
                    continue;
                }
                if (!(grid.contains(newCell))) {
                    grid.put(newCell.getQuadrant(), newCell);
                }
            }
        }
        for (Cell c : grid.values()) {
            addNeighbours(c.getQuadrant());
        }
    }

    private void addNeighbours(XY atPosition) {
        Cell c = grid.get(atPosition);
        int xFactor = (atPosition.getX() - 1) / CELLDISTANCE; //calculates 4 for 105 and 5 for 126
        int yFactor = (atPosition.getY() - 1) / CELLDISTANCE;

        for (int j = -1; j < 2; j++) {
            for (int i = -1; i < 2; i++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (grid.containsKey(new XY(CELLDISTANCE * (i + xFactor) + CELLCENTEROFFSET, CELLDISTANCE * (j + yFactor) + CELLCENTEROFFSET))) {
                    //Doesn't matter because if clause would hinder performance more than just putting it in
                    grid.get(new XY(CELLDISTANCE * (i + xFactor) + CELLCENTEROFFSET, CELLDISTANCE * (j + yFactor) + CELLCENTEROFFSET)).addNeighbour(c);
                }
            }
        }
    }

    public XY cellAt(XY position) {
        //Range from 1<-11->21, 22<-32->42, 43<-53->63, 64<-74->84, 85<-95->105, ...
        //Modulo Operation with 21

        int xFactor = (position.getX() - 1) / CELLDISTANCE; //calculates 4 for 105 and 5 for 126
        int yFactor = (position.getY() - 1) / CELLDISTANCE;

        //Gives the middle position of the cell back by adding 11 after multiplying
        return new XY(CELLDISTANCE * xFactor + CELLCENTEROFFSET, CELLDISTANCE * yFactor + CELLCENTEROFFSET);
    }

    public void expand() throws NoConnectingNeighbourException {

        Cell startingCell = master.getCurrentCell();
        Cell currentCell = startingCell;
        while (true) {
            Cell tentativeCell = currentCell.getConnectingCell();
            if (tentativeCell != null) {
                tentativeCell.setActive(currentCell.getNextCell());
                currentCell.setNextCell(tentativeCell);
                return;
            }
            currentCell = currentCell.getNextCell();
            if (currentCell.equals(startingCell)) {
                throw new NoConnectingNeighbourException();
            }
        }
    }

    public void evenOut() {
        //Todo: Zellen an Board ausrichten
    }

    public void checkAttendance(long round) {
        for (Cell c : grid.values()) {
            //Todo: Check epsilon (now at 3)
            if (c.getLastFeedback() - round > 3) {
                c.setMiniSquirrel(null);
                nextMini = MiniType.REAPER;
            }
        }
    }

    public Cell freeCell() throws FullGridException {
        for (Cell c : grid.values()) {
            if (!c.isActive()) {
                continue;
            }
            if (c.getMiniSquirrel() == null)
                return c;
        }
        throw new FullGridException();
    }

    private boolean validCell(XY coordinate){
        if (coordinate.getX() < 0 || coordinate.getX() > fieldLimit.getX()){
            return false;
        }
        if (coordinate.getY() < 0 || coordinate.getX() > fieldLimit.getY()){
            return false;
        }
        return true;
    }

}
