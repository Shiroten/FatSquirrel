package de.hsa.games.fatsquirrel.botimpls.Shiroten;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.botimpls.Shiroten.ShirotenMaster;
import de.hsa.games.fatsquirrel.botimpls.Shiroten.ShirotenMini;

public class ShirotenFactory implements BotControllerFactory {
    public BotController createMasterBotController(){
        return new ShirotenMaster();
    }

    public BotController createMiniBotController(){
        return new ShirotenMini();
    }
}
