package de.hsa.games.fatsquirrel.botimpls;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.botimpls.Shiroten.ShirotenMaster;
import de.hsa.games.fatsquirrel.botimpls.Shiroten.ShirotenMini;

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
