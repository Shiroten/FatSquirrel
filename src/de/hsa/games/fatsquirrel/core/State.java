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

    public State() {
        this.board = new Board("default.props");
    }

    public State(String configName) {
        this.board = new Board(configName);
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

    public FlattenedBoard flattenBoard() {
        return board.flatten();
    }

    public List<Entity> getEntitySet() {
        return board.getSet().getEntityList();
    }

    public void saveHighScore(String path) {

        Properties properties = new Properties();
        for (Map.Entry<String, ArrayList<Long>> pairs : highscore.entrySet()) {
            properties.setProperty(pairs.getKey(), pairs.getValue().toString());
        }
        try {
            Writer writer = new FileWriter(path);
            properties.store(writer, "FatSquirrel Highscore");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHighScore(String path) {
        Properties properties = new Properties();
        try {
            Reader reader = new FileReader(path);
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String propertiesString;
        String[] valuesToParse;

        for (Object o: properties.stringPropertyNames()) {
            propertiesString = properties.getProperty(o.toString(),"");
            propertiesString = propertiesString.substring(1, propertiesString.length() - 1);
            propertiesString = propertiesString.replaceAll(" ", "");
            valuesToParse = propertiesString.split(",");

            ArrayList values = new ArrayList();
            for(int i = 0;i < valuesToParse.length; i++){
                values.add(Long.parseLong(valuesToParse[i]));
                //System.out.println(Long.parseLong(valuesToParse[i]));
            }
            highscore.put(o.toString(), values);
        }
    }
}

