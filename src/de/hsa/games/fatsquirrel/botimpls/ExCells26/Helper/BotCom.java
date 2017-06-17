package de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini.ExCells26ReaperMini;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini.MiniType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shiroten on 15.06.2017.
 */
public class BotCom {

    private BotController master;
    private List<Cell> grid = new ArrayList<>();

    private MiniType nextMini;
    private XY fieldLimit;
    //Todo: fieldLimit muss durch das MasterSquirrel auf startPositions view gesetzt werden.
    private boolean fieldLimitFound;
    private XY startPositionOfMaster;
    private static final int CELLDISTANCE = 20;

    public BotController getMaster() {
        return master;
    }

    public void setMaster(BotController master) {
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

    public void setStartPositionOfMaster(XY startPositionOfMaster) {
        this.startPositionOfMaster = startPositionOfMaster;
    }

    public void init() {
        Cell firstCell = new Cell(cellAt(startPositionOfMaster));


        //Todo: FeldzuKlein abfangen (Priorität niedrig)
        //Todo: Ersten 4 Grids berechnen
        //Todo: Sieb mit Vektoren (-1,0) und (0,-1)
    }

    public void getAllCells() {
        int xLimit = (fieldLimit.getX() - 1) / 21;
        int yLimit = (fieldLimit.getY() - 1) / 21;

        for (int i = 0; i < xLimit; i++) {
            for (int j = 0; j < yLimit; j++) {
                Cell newCell = new Cell(new XY(11 + 20 * i, 11 + 20 * j));
                //TODO: Zelle Array hinzufügen
            }
        }
        //Todo: unfinished Cells
        //Fügt noch nicht bekannte Cellen hinzu
        //anhand der fieldLimit berechen
    }

    public XY toName(XY cellCoordinate, int factor) {

        if (factor <= 0)
            return null;
        for (int i = 1; i < factor; i++) {
            for (int j = 1; j < factor; j++) {
                //boolean ergebnis = usableCell(cellCoordinate,i,j);


            }
        }

        //Todo: maybe unnecessary
        //wenn x unter 0 und usableCell == false, dann +1 ansonsten -1 zu setzen
        //y analog

        //x und y in kombination aus positiv und negativ
        //für x und y den vector bestimmen und mit 45 gegen uhrzeigersinn drehen
        return null;
    }

    /**
     * Calculates a valid cell position by adding a vector to
     *
     * @param cellCoordinate
     * @param xFactor
     * @param yFactor
     * @return valid cell position calculated by given params
     */
    private XY usableCell(XY cellCoordinate, int xFactor, int yFactor) {
        //Todo: needs complett workover
        int x, y;
        x = cellCoordinate.getX() - 20 * xFactor < 0 ? 1 : -1;
        y = cellCoordinate.getY() - 20 * yFactor < 0 ? 1 : -1;

        XY directionToNextCell = XYsupport.rotate(XYsupport.Rotation.anticlockwise, new XY(x, y), 1);
        return new XY(directionToNextCell.getX() * xFactor, directionToNextCell.getY() * yFactor).times(CELLDISTANCE);
    }

    public XY cellAt(XY position) {
        //Range from 1<-11->21, 22<-32->42, 43<-53->63, 64<-74->84, 85<-95->105, ...
        //Modulo Operation with 21

        int xFactor = (position.getX() - 1) / 21; //calculates 4 for 105 and 5 for 126
        int yFactor = (position.getY() - 1) / 21;

        //Gives the middle position of the cell back by adding 11 after multiplying
        return new XY(21 * xFactor + 11, 21 * yFactor + 11);
    }

    public void expand() {
        //Todo: Grid Überlauf händeln (siehe addMini())
    }

    public void evenOut() {
        //Todo: Zellen an Board ausrichten
    }

    public void checkAttendance(long round) {
        for (Cell c : grid) {
            //Todo: Check epsilon (now at 3)
            if (c.getLastFeedback() - round > 3) {
                c.setMiniSquirrel(null);
                nextMini = MiniType.REAPER;
            }
        }
    }

    public Cell addMini(ExCells26ReaperMini mini) {
        try {
            Cell freeCell = freeCell();
            freeCell.setMiniSquirrel(mini);
            return freeCell;
        } catch (FullGridException e) {
            expand();
            //Rekursiver Aufruf der Methode da durch expand() mehr Grids zur verfügung stehen
            return addMini(mini);
        }
    }

    public Cell freeCell() throws FullGridException {
        for (Cell c : grid) {
            if (c.getMiniSquirrel() == null)
                return c;
        }
        throw new FullGridException();
    }

}
