package de.hsa.games.fatsquirrel.botimpls.ExCells26;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.BotController;

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
    private XY startPositionOfMaster;

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

    public MiniType getNextMini(){
        return nextMini;
    }

    public void setStartPositionOfMaster(XY startPositionOfMaster) {
        this.startPositionOfMaster = startPositionOfMaster;
    }

    public void init() {
        //Todo: FeldzuKlein abfangen (Priorität niedrig)
        //Todo: Ersten 4 Grids berechnen
        //Todo: Sieb mit Vektoren (-1,0) und (0,-1)
    }

    public void expand() {
        //Todo: Grid Überlauf händeln (siehe addMini())
    }

    public void evenOut(){
        //Todo: Zellen an Board ausrichten
    }

    public void checkAttendance(long round){
        for(Cell c : grid){
            //Todo: Check epsilon (now at 3)
            if (c.getLastFeedback() - round > 3){
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
