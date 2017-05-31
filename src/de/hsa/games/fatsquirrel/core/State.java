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

    private Map<String, ArrayList<Long>> highscore = new HashMap<>();
    private Board board;

    public State() {
        BoardConfig config = new BoardConfig(new XY(12, 12), 1, 1, 4, 3, 3);
        this.board = new Board(config);
    }

    //TODO: So schachteln, dass nur noch über State das Board erzeugt wird
    public State(Board board) {
        this.board = board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public void update() {

        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINER, "start update() from State");


        FlattenedBoard flat = board.flatten();
        flat.tickImplosions();
        board.getSet().nextStep(flat);

    }

    public String printFinalHighscore() {
        for (MasterSquirrel ms : board.getMasterSquirrel()) {
            if (ms == null) {
                continue;
            }
            ArrayList longArrayList;
            if (highscore.get(ms.getEntityName()) != null) {
                longArrayList = highscore.get(ms.getEntityName());
            } else {
                longArrayList = new ArrayList();
            }
            longArrayList.add((long) ms.getEnergy());
            highscore.put(ms.getEntityName(), longArrayList);
        }

        String output = "";
        if (board.getRemainingGameTime() % 20 == 0) {
            for (ArrayList<Long> entry : highscore.values()) {
                Collections.sort(entry);
            }
            for (Map.Entry<String, ArrayList<Long>> pairs : highscore.entrySet()) {
                long average = 0;
                output = output + pairs.getKey() + ": ";
                for (Long entry : pairs.getValue()) {
                    output = output + entry + " | ";
                    average = average + entry;
                }
                output = output + "Average: " + average / pairs.getValue().size() + String.format("%n");
            }
        }
        return output;
    }

    public FlattenedBoard flattenBoard() {
        return board.flatten();
    }

    public List<Entity> getEntitySet() {
        return board.getSet().getEntityList();
    }
}
