package de.hsa.games.fatsquirrel.botimpls;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.botimpls.GoodBeastChaser.GoodBeastChaserMaster;
import de.hsa.games.fatsquirrel.botimpls.GoodBeastChaser.GoodBeastChaserMini;

public class GoodBeastChaserFactory implements BotControllerFactory {
    public BotController createMasterBotController() {
        return new GoodBeastChaserMaster();
    }

    public BotController createMiniBotController() {
        return new GoodBeastChaserMini();
    }
}
