package de.hsa.games.fatsquirrel.botapi.bots.Player;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;

/**
 * Created by tillm on 15.05.2017.
 */
public class PlayerFactory implements BotControllerFactory {
    public BotController createMasterBotController(){
        return new PlayerMaster();
    }

    public BotController createMiniBotController(){
        return new PlayerMini();
    }
}
