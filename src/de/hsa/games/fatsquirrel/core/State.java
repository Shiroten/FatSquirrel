package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.entity.Entity;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class State {

    private Map<String, Long> highscore = new HashMap<>();
    private Board board;

    public State() {
        BoardConfig config = new BoardConfig(new XY(12, 12), 1, 1, 4, 3, 3);
        this.board = new Board(config);
    }

    public State(Board board) {
        this.board = board;
    }


    public Board getBoard() {
        return board;
    }

    public Map<String, Long> getHighscore() {
        return highscore;
    }

    public void update() {

        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINER, "start update() from State");


        FlattenedBoard flat = board.flatten();
        flat.tickImplosions();
        board.getSet().nextStep(flat);

        calcHighscore();
    }

    private void calcHighscore() {
        for (MasterSquirrel ms : board.getMasterSquirrel()) {
            if (ms != null)
                highscore.put(ms.getEntityName(), (long) ms.getEnergy());
        }

        if (board.getRemainingGameTime() % 20 == 0) {
            SortedMap sm = new TreeMap();
            for (Map.Entry<String, Long> entry : highscore.entrySet()) {
                sm.put(entry.getKey(), entry.getValue());

            }
            System.out.printf(sm.toString());
            System.out.println();
        }
    }

    public FlattenedBoard flattenBoard() {
        return board.flatten();
    }

    public List<Entity> getEntitySet() {
        return board.getSet().getEntityList();
    }
}
