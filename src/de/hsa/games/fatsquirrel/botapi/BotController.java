package de.hsa.games.fatsquirrel.botapi;

public interface BotController {
    /**
     * The next action the bot will take
     * @param view The information and actions the bot has at disposal
     */
    void nextStep(ControllerContext view);
}
