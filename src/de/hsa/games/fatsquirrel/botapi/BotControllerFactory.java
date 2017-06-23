package de.hsa.games.fatsquirrel.botapi;

/**
 * Returns BotController for an ai. Every AI has to implement its own Factory
 */
public interface BotControllerFactory {
    /**
     * Create a new BotController for a MiniSquirrel
     * @return the BotController-Implementation for the MiniSquirrel
     */
    BotController createMiniBotController();
    /**
     * Create a new BotController for a Mastersquirrel
     * @return the BotController-Implementation for the MasterSquirrel
     */
    BotController createMasterBotController();
}
