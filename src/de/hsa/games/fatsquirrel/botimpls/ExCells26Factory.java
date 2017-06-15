package de.hsa.games.fatsquirrel.botimpls;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.*;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper.BotCom;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini.ExCells26BombMini;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini.ExCells26ReaperMini;
import de.hsa.games.fatsquirrel.botimpls.ExCells26.Mini.ExCells26ReconMini;


public class ExCells26Factory implements BotControllerFactory {

    private BotCom botcom;

    public ExCells26Factory() {
        botcom = new BotCom();
        //Initialisierung von BotCom und allen wichtigen Klassen
    }

    public BotController createMasterBotController() {
        ExCells26Master master = new ExCells26Master(botcom);
        botcom.setMaster(master);
        botcom.init();
        //Referenzen Setzen
        return master;
    }

    public BotController createMiniBotController() {
        //Todo: Alternative, ein MiniSquirrel das unterschiedliche Kis in abhängigkeit einer Einstellung verwendet

        BotController mini;
        switch (botcom.getNextMini()) {
            case RECON:
                mini = new ExCells26ReconMini();
                break;
            case REAPER:
                mini = new ExCells26ReaperMini(botcom);
                break;
            case BOMB:
                mini = new ExCells26BombMini();
                break;
            default:
                mini = new ExCells26ReaperMini(botcom);
        }
        return mini;
    }
}
