package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.State;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {

    private double gameSpeed;
    private double gameSpeedScaleFactor = 1.3;
    private UI ui;
    private State state;
    protected ActionCommand command;

    public UI getUi() {
        return ui;
    }

    protected void setUi(UI ui) {
        this.ui = ui;
    }


    protected State getState() {
        return state;
    }

    protected void setState(State state) {
        this.state = state;
    }

    public void setGameSpeed(double getTICKLENGTH) {
        this.gameSpeed = (Math.log(getTICKLENGTH) / Math.log(gameSpeedScaleFactor));
    }

    public void addGameSpeed(int gameSpeed) {
        if ((int) Math.pow((this.gameSpeed + gameSpeed), gameSpeedScaleFactor) > 1)
            this.gameSpeed = this.gameSpeed + gameSpeed;
    }

    public int getTickLength() {
        return (int) Math.pow(gameSpeedScaleFactor, gameSpeed);
    }

    public Game(State state, UI ui) {
        this.state = state;
        this.ui = ui;
    }

    public Game() {
    }

    public void run() {
        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINER, "start render()");
        render();
        logger.log(Level.FINER, "start update()");
        update();

    }

    public void startSingleThreadGame() {
        while (true) {
            render();
            processInput();
            update();
        }
    }
    protected void reset(){
        Board board = new Board(state.getBoard().getConfig());
        state = new State(board);
    }

    protected void processInput() {

    }

    protected void render() {

    }

    protected void update() {

    }

    public enum GameType {
        SINGLE_PLAYER, WITH_BOT, BOT_ONLY, PACMAN;
    }
}
