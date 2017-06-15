package de.hsa.games.fatsquirrel.botapi;

/**
 * Defines the action call of ai controlled squirrels
 */
public interface BotController {
    /**
     * The next action the bot will take
     * @param view The information and actions the bot has at disposal
     */
    void nextStep(ControllerContext view);
}
