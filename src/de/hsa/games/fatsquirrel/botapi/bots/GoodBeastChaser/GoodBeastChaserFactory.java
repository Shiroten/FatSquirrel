package de.hsa.games.fatsquirrel.botapi.bots.GoodBeastChaser;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;

public class GoodBeastChaserFactory implements BotControllerFactory {
    public BotController createMasterBotController(){
        return new GoodBeastChaserMaster();
    }

    public BotController createMiniBotController(){
        return new GoodBeastChaserMini();
    }
}
