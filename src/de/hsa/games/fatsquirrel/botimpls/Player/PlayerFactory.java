package de.hsa.games.fatsquirrel.botimpls.Player;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.botimpls.Player.PlayerMaster;
import de.hsa.games.fatsquirrel.botimpls.Player.PlayerMini;

public class PlayerFactory implements BotControllerFactory {
    public BotController createMasterBotController(){
        return new PlayerMaster();
    }

    public BotController createMiniBotController(){
        return new PlayerMini();
    }
}
