package de.hsa.games.fatsquirrel.botimpls.Baster;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botimpls.Baster.BasterMaster;
import de.hsa.games.fatsquirrel.botimpls.Baster.BasterMini;

public class BasterFactory implements de.hsa.games.fatsquirrel.botapi.BotControllerFactory{

    public BasterFactory(){

    }

    public BotController createMasterBotController(){
        return  new BasterMaster();
    }

    public BotController createMiniBotController(){
        return new BasterMini();
    }
}
