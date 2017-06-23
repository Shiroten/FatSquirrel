package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Collection of all the Settings in the Game
 */
public class BoardConfig {

    private final XY size;

    private final int numberOfGb;
    private final int numberOfBb;
    private final int numberOfGp;
    private final int numberOfBp;
    private final int numberOfWa;

    private final int pointsForMiniSquirrel = 150;

    private final long gameDurationAtStart;
    private final int ticklength;
    private final int squirrelStunTimeInTicks = 3;
    private final int beastMoveTimeInTicks = 4;
    private final int miniSquirrelMoveTimeInTicks = 1;

    private final int viewDistanceOfGoodbeast;
    private final int viewDistanceOfBadbeast;

    private final Game.GameType gameType;
    private final String[] bots;

    private final int SELLSIZE;
    private final String configName;

    private void saveConfig(){
        Properties properties = new Properties();
        properties.setProperty("Size", size.toString());
        properties.setProperty("Number_GoodBeasts", "" + numberOfGb);
        properties.setProperty("Number_BadBeasts", "" + numberOfBb);
        properties.setProperty("Number_GoodPlants", "" + numberOfGp);
        properties.setProperty("Number_BadPlants", "" + numberOfBp);
        properties.setProperty("Number_Walls", "" + numberOfWa);

        properties.setProperty("Points_For_MiniSquirrel", "" + pointsForMiniSquirrel);

        properties.setProperty("Game_Duration", "" + gameDurationAtStart);
        properties.setProperty("Ticklength", "" + ticklength);
        properties.setProperty("Squirrel_Stun_Time_Ticks", "" + squirrelStunTimeInTicks);
        properties.setProperty("Beast_Move_Time_Ticks", "" + beastMoveTimeInTicks);
        properties.setProperty("MiniSquirrel_Move_Time_Ticks", "" + miniSquirrelMoveTimeInTicks);

        properties.setProperty("View_Distance_GoodBeast", ""+ viewDistanceOfGoodbeast);
        properties.setProperty("View_Distance_BadBeast", "" + viewDistanceOfBadbeast);

        properties.setProperty("GameMode", gameType.toString());

        try {
            Writer writer = new FileWriter("default.props");
            properties.store(writer, "FatSquirrel");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    BoardConfig(String filename){
        Properties properties = new Properties();
        try {
            Reader reader = new FileReader(filename);
            properties.load(reader);
        } catch (IOException e){
            Logger logger = Logger.getLogger(Launcher.class.getName());
            logger.log(Level.FINE, "Properties konnten nicht geladen werden");
        }

        //System.out.println(properties.getProperty("Size"));
        this.size = XYsupport.stringToXY(properties.getProperty("Size", "x: 40 y:40"));
        numberOfGb = Integer.parseInt(properties.getProperty("Number_GoodBeasts", "15"));
        numberOfBb = Integer.parseInt(properties.getProperty("Number_BadBeasts", "5"));
        numberOfGp = Integer.parseInt(properties.getProperty("Number_GoodPlants", "20"));
        numberOfBp = Integer.parseInt(properties.getProperty("Number_BadPlants", "6"));
        numberOfWa = Integer.parseInt(properties.getProperty("Number_Walls", "10"));

        //pointsForMiniSquirrel = Integer.parseInt(properties.getProperty("Points_For_MiniSquirrel"));

        gameDurationAtStart = Long.parseLong(properties.getProperty("Game_Duration", "1800"));
        ticklength = Integer.parseInt(properties.getProperty("Ticklength", "10"));
        //squirrelStunTimeInTicks = Integer.parseInt(properties.getProperty("Squirrel_Stun_Time_Ticks"));
        properties.getProperty("Beast_Move_Time_Ticks", "4");
        properties.getProperty("MiniSquirrel_Move_Time_Ticks", "1");

        viewDistanceOfGoodbeast = Integer.parseInt(properties.getProperty("View_Distance_GoodBeast", "7"));
        viewDistanceOfBadbeast = Integer.parseInt(properties.getProperty("View_Distance_BadBeast", "7"));

        gameType = Game.GameType.getGameType(properties.getProperty("GameMode", "WITH_BOT"));

        SELLSIZE = Integer.parseInt(properties.getProperty("SellCize", "25"));
        configName = filename;

        bots = readInBots();
    }

    Game.GameType getGameType() {
        return gameType;
    }

    public XY getSize() {
        return size;
    }

    public int getTicklength() {
        return ticklength;
    }

    int getPointsForMiniSquirrel() {
        return pointsForMiniSquirrel;
    }

    int getSquirrelStunTimeInTicks() {
        return squirrelStunTimeInTicks;
    }

    int getBeastMoveTimeInTicks() {
        return beastMoveTimeInTicks;
    }

    int getMiniSquirrelMoveTimeInTicks() {
        return miniSquirrelMoveTimeInTicks;
    }

    int getViewDistanceOfGoodbeast() {
        return viewDistanceOfGoodbeast;
    }

    int getViewDistanceOfBadbeast() {
        return viewDistanceOfBadbeast;
    }

    int getNumberOfGb() {
        return numberOfGb;
    }

    int getNumberOfBb() {
        return numberOfBb;
    }

    int getNumberOfGp() {
        return numberOfGp;
    }

    int getNumberOfBp() {
        return numberOfBp;
    }

    int getNumberOfWa() {
        return numberOfWa;
    }

    int getNUMBER_OF_BOTS() {
        return bots.length;
    }

    public long getGameDurationAtStart() {
        return gameDurationAtStart;
    }

    public int getSELLSIZE() {
        return SELLSIZE;
    }

    String[] getBots(){
        return bots;
    }

    public String getConfigName() {
        return configName;
    }

    private String[] readInBots(){

        String osVersion = System.getProperty("os.name");
        boolean isLinux;
        isLinux = osVersion.contentEquals("Linux");

        File folder;
        if (isLinux){
            folder = new File("src/de/hsa/games/fatsquirrel/botimpls");
        }else{
            folder = new File("src\\de\\hsa\\games\\fatsquirrel\\botimpls");
        }

        File[] listOfBots = folder.listFiles((dir, name) -> name.substring(name.length()-4, name.length()).equals("java"));
        String[] bots;

        if(listOfBots != null) {
            bots = new String[listOfBots.length];
            for (int i = 0; i < listOfBots.length; i++) {
                if (listOfBots[i].isFile()) {
                    // \\\\ = \   | Beim Regex muss ein Backslash mit drei Backslashes escaped werden
                    String s;
                    if (isLinux){
                        s = listOfBots[i].toString().split("/")[6];
                    }else{
                       s = listOfBots[i].toString().split("\\\\")[6];
                    }
                    bots[i] = s.substring(0, s.length()-5);
                }
            }
            return bots;
        }
        return null;
    }

}
