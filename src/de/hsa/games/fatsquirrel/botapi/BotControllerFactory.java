package de.hsa.games.fatsquirrel.botapi;

/**
 * Returns BotController for an ai. Every AI has to implement its own Factory
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
