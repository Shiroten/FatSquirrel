package de.hsa.games.fatsquirrel.botapi.bots.Baster;

import de.hsa.games.fatsquirrel.botapi.BotController;

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
