package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.entity.Entity;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class State {

    private Map<String, ArrayList<Long>> highscore = new HashMap<>();
    private Board board;

    /**
     * Creates new State with default Board
     */
    public State() {
        this.board = new Board("default.props");
    }

    /**
     * Creates new State with BoardName
     *
     * @param configName
     */
    public State(String configName) {
        this.board = new Board(configName);
    }

    /**
     * Sets new Board to State
     *
     * @param board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * @return the Board of the State
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Calls the nextStep() of the EntitySet and tick each Implosion Counter
     */
    public void update() {

        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINER, "start update() from State");


        FlattenedBoard flat = board.flatten();
        flat.tickImplosions();
        board.getSet().nextStep(flat);

    }

    /**
     * Set the actual Highscore
     */
    public void updateHighscore() {
        //Get All MasterSquirrel
        for (MasterSquirrel ms : board.getMasterSquirrel()) {
            if (ms == null) {
                continue;
            }
            ArrayList longArrayList;
            if (highscore.get(ms.getEntityName()) != null) {
                longArrayList = highscore.get(ms.getEntityName());
            } else {
                //if first Value, create new list
                longArrayList = new ArrayList();
            }
            longArrayList.add((long) ms.getEnergy());
            //put list back to map
            highscore.put(ms.getEntityName(), longArrayList);
        }

    }

    /**
     * @return String of the actual HighSore
     */
    public String printHighscore() {
        //TODO: StringBuilder
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

    /**
     * @return flatten the Board and Return the FlattenBoard equivalent
     */
    public FlattenedBoard flattenBoard() {
        return board.flatten();
    }

    /**
     * @return the EntitySet of the Board under the State
     */
    public List<Entity> getEntitySet() {
        return board.getSet().getEntityList();
    }

    /**
     * @param path save the actual HighScore to a File in the given Path
     */
    public void saveHighScore(String path) {
        //Sort Array before saving
        for (ArrayList<Long> entry : highscore.values()) {
            Collections.sort(entry);
        }

        Properties properties = new Properties();
        //Get all highscore Values
        for (Map.Entry<String, ArrayList<Long>> pairs : highscore.entrySet()) {
            //Set Properties according to Key and Values
            properties.setProperty(pairs.getKey(), pairs.getValue().toString());
        }
        try {
            Writer writer = new FileWriter(path);
            properties.store(writer, "FatSquirrel Highscore");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param path load the actual HighScore from a File in the given Path
     */
    public void loadHighScore(String path) {
        //OpenFile
        Properties properties = new Properties();
        try {
            Reader reader = new FileReader(path);
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String propertiesString;
        String[] valuesToParse;

        for (Object o : properties.stringPropertyNames()) {
            //Get String like [1000, 1000, 23900, 33549, 34749, 37287]
            propertiesString = properties.getProperty(o.toString(), "");
            //Reduce to 1000, 1000, 23900, 33549, 34749, 37287
            propertiesString = propertiesString.substring(1, propertiesString.length() - 1);
            //Get rid of spaces
            propertiesString = propertiesString.replaceAll(" ", "");
            //Split to seperate Values
            valuesToParse = propertiesString.split(",");

            ArrayList values = new ArrayList();
            for (int i = 0; i < valuesToParse.length; i++) {
                values.add(Long.parseLong(valuesToParse[i]));
            }
            highscore.put(o.toString(), values);
        }
    }
}

