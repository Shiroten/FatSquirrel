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

    public Hashtable<XY, Cell> getGrid() {
        return grid;
    }

    public Hashtable<XY, Cell> grid = new Hashtable<>();

    private MiniType nextMiniTypeToSpawn;

    private Cell cellForNextMini;
    private XY fieldLimit;

    //Todo: fieldLimit muss durch das MasterSquirrel auf startPositions view gesetzt werden.
    private boolean fieldLimitFound;
    public XY startPositionOfMaster;
    public XY positionOfExCellMaster;

    //Default: 21 (last working number)
    int cellDistanceX = 21;
    int cellDistanceY = 21;

    //Default: 11 (last working number)
    private int cellCenterOffsetX = 7;
    private int cellCenterOffsetY = 7;

    //Default: 21 (last working number)
    private int cellsize = 15;

    public int getCellDistanceX() {
        return cellDistanceX;
    }

    public int getCellDistanceY() {
        return cellDistanceY;
    }

    public BotController getMaster() {
        return master;
    }

    public void setMaster(ExCells26Master master) {
        this.master = master;
    }

    public void setNextMiniTypeToSpawn(MiniType nextMiniTypeToSpawn) {
        this.nextMiniTypeToSpawn = nextMiniTypeToSpawn;
    }

    public XY getFieldLimit() {
        return fieldLimit;
    }

    public void setFieldLimit(XY fieldLimit) {
        this.fieldLimit = fieldLimit;
    }

    public MiniType getNextMiniTypeToSpawn() {
        return nextMiniTypeToSpawn;
    }

    public int getCellsize() {
        return cellsize;
    }

    public void setCellsize(int cellsize) {
        this.cellsize = cellsize;
    }

    public boolean isFieldLimitFound() {
        return fieldLimitFound;
    }

    public void setFieldLimitFound(boolean fieldLimitFound) {
        this.fieldLimitFound = fieldLimitFound;
    }

    public void setStartPositionOfMaster(XY startPositionOfMaster) {
        this.startPositionOfMaster = startPositionOfMaster;
    }

    public Cell getCellForNextMini() {
        return cellForNextMini;
    }

    public void setCellForNextMini(Cell cellForNextMini) {
        this.cellForNextMini = cellForNextMini;
    }

    public void init() {

        //Todo: Bug found with no starting cells calculated.
        calculateCellSize();
        getAllCells();

        Cell firstCell = grid.get(cellAt(startPositionOfMaster));
        if (firstCell == null) {
            System.out.println("Fieldlimit " + fieldLimit + " Master: " + startPositionOfMaster);
            firstCell = grid.values().iterator().next();
        }
        master.setCurrentCell(firstCell);

        firstCell.setActive(firstCell);
    }

    public void calculateCellSize(){
        int newCellDistanceX = cellDistanceX;
        int newCellDistanceY = cellDistanceY;

        for(int i = 0; i < cellDistanceX; i++){
            if(fieldLimit.getX() % (newCellDistanceX- i) == 0){
                cellDistanceX = newCellDistanceX - i;
                break;
            } else if(fieldLimit.getX() % (newCellDistanceX+ i) == 0){
                cellDistanceX = newCellDistanceX + i;
                break;
            }
        }

        for(int i = 0; i < cellDistanceY; i++){
            if(fieldLimit.getY() % (newCellDistanceY- i) == 0){
                cellDistanceY = newCellDistanceY - i;
                break;
            } else if(fieldLimit.getY() % (newCellDistanceY+ i) == 0){
                cellDistanceY = newCellDistanceY + i;
                break;
            }
        }

        cellCenterOffsetX = (cellDistanceX / 2) + 1;
        cellCenterOffsetY = (cellDistanceY / 2) + 1;
    }

    public void getAllCells() {
        int xLimit = (fieldLimit.getX() - 1) / cellDistanceX;
        int yLimit = (fieldLimit.getY() - 1) / cellDistanceY;

        for (int i = 0; i <= xLimit; i++) {
            for (int j = 0; j <= yLimit; j++) {
                Cell newCell = new Cell(new XY(cellCenterOffsetX + cellDistanceX * i, cellCenterOffsetY + cellDistanceY * j));
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
        int xFactor = (atPosition.getX() - 1) / cellDistanceX; //calculates 4 for 105 and 5 for 126
        int yFactor = (atPosition.getY() - 1) / cellDistanceY;

        for (int j = -1; j < 2; j++) {
            for (int i = -1; i < 2; i++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (grid.containsKey(new XY(cellDistanceX * (i + xFactor) + cellCenterOffsetX, cellDistanceY * (j + yFactor) + cellCenterOffsetY))) {
                    //Doesn't matter because if clause would hinder performance more than just putting it in
                    grid.get(new XY(cellDistanceX * (i + xFactor) + cellCenterOffsetX, cellDistanceY * (j + yFactor) + cellCenterOffsetY)).addNeighbour(c);
                }
            }
        }
    }

    public XY cellAt(XY position) {
        //Range from 1<-11->21, 22<-32->42, 43<-53->63, 64<-74->84, 85<-95->105, ...
        //Modulo Operation with 21

        int xFactor = (position.getX() - 1) / cellDistanceX; //calculates 4 for 105 and 5 for 126
        int yFactor = (position.getY() - 1) / cellDistanceY;

        //returns the middle position of the cell by adding 11 after multiplying
        return new XY(cellDistanceX * xFactor + cellCenterOffsetX, cellDistanceY * yFactor + cellCenterOffsetY);
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
                nextMiniTypeToSpawn = MiniType.REAPER;
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

    private boolean validCell(XY coordinate) {
        return !(coordinate.getX() < 0 || coordinate.getX() > fieldLimit.getX()) && !(coordinate.getY() < 0 || coordinate.getY() > fieldLimit.getY());
    }

}
