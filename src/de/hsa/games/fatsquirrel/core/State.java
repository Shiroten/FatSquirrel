package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.entity.Entity;

import java.util.logging.Level;
import java.util.logging.Logger;

public class State {
    private int highscore;
    private Board board;

    public State() {
        BoardConfig config = new BoardConfig(new XY(12, 12), 1, 1, 4, 3, 3);
        this.board = new Board(config);
    }

    public State(Board board) {
        this.board = board;
    }

    public State(Object ... params) {
        //this.board = new Board(new BoardConfig(params));
    }

    public Board getBoard() {
        return board;
    }

    public int getHighscore() {
        return highscore;
    }

    public void update() {

        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINER, "start update() from State");


        FlattenedBoard flat = board.flatten();
        flat.tickImplosions();
        board.getSet().nextStep(flat);
    }

    public FlattenedBoard flattenBoard() {
        return board.flatten();
    }

    public Entity[] getEntitySet() {
        return board.getSet().getEntityList();
    }
}
