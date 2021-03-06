package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.State;
import de.hsa.games.fatsquirrel.core.entity.squirrels.HandOperatedMasterSquirrel;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Actually runs the game by looping run, processing, and updating
 */
public class Game {

    protected HandOperatedMasterSquirrel handOperatedMasterSquirrel;
    private double gameSpeed;
    private final double gameSpeedScaleFactor = 1.3;
    private UI ui;
    private State state;
    protected ActionCommand command;

    /**
     * @return the UI of the Game
     */
    public UI getUi() {
        return ui;
    }

    /**
     * @param ui set the UI of the Game
     */
    protected void setUi(UI ui) {
        this.ui = ui;
    }

    /**
     * @return the state from the Game
     */
    protected State getState() {
        return state;
    }

    /**
     * @param state sets new state to the Game
     */
    protected void setState(State state) {
        this.state = state;
    }

    /**
     * set the GameSpeed of the Game in Ticks
     *
     * @param getTICKLENGTH tickLength in milliSeconds
     */
    public void setGameSpeed(double getTICKLENGTH) {
        this.gameSpeed = (Math.log(getTICKLENGTH) / Math.log(gameSpeedScaleFactor));
    }

    /**
     * Increased the GameSpeed by a logarithmic amount
     *
     * @param gameSpeed gameSpeed delta to add to the actual amount
     */
    public void addGameSpeed(int gameSpeed) {
        if ((int) Math.pow((this.gameSpeed + gameSpeed), gameSpeedScaleFactor) > 1)
            this.gameSpeed = this.gameSpeed + gameSpeed;
    }

    /**
     * Gets the actual TickLength of the Game
     *
     * @return the actual tickLength
     */
    public int getTickLength() {
        return (int) Math.pow(gameSpeedScaleFactor, gameSpeed);
    }

    /**
     * Creates the Game with UI and State
     *
     * @param state set the state for Game
     * @param ui set the UI for Game
     */
    public Game(State state, UI ui) {
        this.state = state;
        this.ui = ui;
    }

    /**
     * Empty Constructor
     */
    public Game() {
    }

    /**
     * Runs render() and update() with logging
     */
    void run() {
        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINER, "start render()");
        render();
        logger.log(Level.FINER, "start update()");
        update();

    }

    /**
     * Useless Method
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void startSingleThreadGame() {
        while (true) {
            render();
            processInput();
            update();
        }
    }

    /**
     * Resets the actual Game and overrides the Board with a new one
     */
    void reset() {
        Board board = new Board(state.getBoard().getConfig().getConfigName());
        state.setBoard(board);
        handOperatedMasterSquirrel = this.getState().getBoard().getHandOperatedMasterSquirrel();
    }

    protected void processInput() {

    }

    protected void render() {

    }

    protected void update() {

    }

    /**
     * Switch for single play or with bots
     */
    public enum GameType {
        SINGLE_PLAYER, WITH_BOT, BOT_ONLY;

        public static GameType getGameType(String s) {
            s = s.toUpperCase();
            switch (s) {
                case "SINGLE_PLAYER":
                    return SINGLE_PLAYER;
                case "WITH_BOT":
                    return WITH_BOT;
                case "BOT_ONLY":
                    return BOT_ONLY;
                default:
                    return null;
            }
        }
    }
}
