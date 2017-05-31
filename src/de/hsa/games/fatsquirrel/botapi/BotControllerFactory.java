package de.hsa.games.fatsquirrel.botapi;

/**
 * Created by tillm on 09.05.2017.
 * Every AI implements its own Factory
 */
public interface BotControllerFactory {
    /**
     * Create a new Botcontroller for a MiniSquirrel
     * @return the BotController-Implementation for the Minisquirrel
     */
    BotController createMiniBotController();
    /**
     * Create a new Botcontroller for a Mastersquirrel
     * @return the BotController-Implementation for the Mastersquirrel
     */
    BotController createMasterBotController();
}
