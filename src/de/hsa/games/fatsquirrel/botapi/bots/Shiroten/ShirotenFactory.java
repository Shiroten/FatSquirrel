package de.hsa.games.fatsquirrel.botapi.bots.Shiroten;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;

/**
 * Created by tillm on 15.05.2017.
 */
public class ShirotenFactory implements BotControllerFactory {
    public BotController createMasterBotController(){
        return new ShirotenMaster();
    }

    public BotController createMiniBotController(){
        return new ShirotenMini();
    }
}
