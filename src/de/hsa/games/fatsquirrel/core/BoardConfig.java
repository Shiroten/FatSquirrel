package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;

import java.io.*;
import java.util.Properties;

/**
 * Collection of all the Settings in the Game
 */
public class BoardConfig {

    private final XY size;

    //TODO: klein schreiben
    private final int NUMBER_OF_GB;
    private final int NUMBER_OF_BB;
    private final int NUMBER_OF_GP;
    private final int NUMBER_OF_BP;
    private final int NUMBER_OF_WA;

    private final int POINTS_FOR_MINI_SQUIRREL = 150;

    private final long GAME_DURATION_AT_START;
    private final int TICKLENGTH;
    private final int SQUIRREL_STUN_TIME_IN_TICKS = 3;
    private final int BEAST_MOVE_TIME_IN_TICKS = 4;
    private final int MINI_SQUIRREL_MOVE_TIME_IN_TICKS = 1;

    private final int VIEW_DISTANCE_OF_GOODBEAST;
    private final int VIEW_DISTANCE_OF_BADBEAST;

    private final Game.GameType gameType;
    private final String[] bots;

    private final int SELLSIZE;
    private final String configName;

    private void saveConfig(){
        Properties properties = new Properties();
        properties.setProperty("Size", size.toString());
        properties.setProperty("Number_GoodBeasts", "" + NUMBER_OF_GB);
        properties.setProperty("Number_BadBeasts", "" + NUMBER_OF_BB);
        properties.setProperty("Number_GoodPlants", "" + NUMBER_OF_GP);
        properties.setProperty("Number_BadPlants", "" + NUMBER_OF_BP);
        properties.setProperty("Number_Walls", "" + NUMBER_OF_WA);

        properties.setProperty("Points_For_MiniSquirrel", "" + POINTS_FOR_MINI_SQUIRREL);

        properties.setProperty("Game_Duration", "" + GAME_DURATION_AT_START);
        properties.setProperty("Ticklength", "" + TICKLENGTH);
        properties.setProperty("Squirrel_Stun_Time_Ticks", "" + SQUIRREL_STUN_TIME_IN_TICKS);
        properties.setProperty("Beast_Move_Time_Ticks", "" + BEAST_MOVE_TIME_IN_TICKS);
        properties.setProperty("MiniSquirrel_Move_Time_Ticks", "" +MINI_SQUIRREL_MOVE_TIME_IN_TICKS);

        properties.setProperty("View_Distance_GoodBeast", ""+ VIEW_DISTANCE_OF_GOODBEAST);
        properties.setProperty("View_Distance_BadBeast", "" + VIEW_DISTANCE_OF_BADBEAST);

        properties.setProperty("GameMode", gameType.toString());

        try {
            Writer writer = new FileWriter("default.props");
            properties.store(writer, "FatSquirrel");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public BoardConfig(String filename){
        Properties properties = new Properties();
        try {
            Reader reader = new FileReader(filename);
            properties.load(reader);
        } catch (IOException e){
            //TODO: in Log schreiben
            e.printStackTrace();
        }

        //System.out.println(properties.getProperty("Size"));
        this.size = XYsupport.stringToXY(properties.getProperty("Size", "x: 40 y:40"));
        NUMBER_OF_GB = Integer.parseInt(properties.getProperty("Number_GoodBeasts", "15"));
        NUMBER_OF_BB = Integer.parseInt(properties.getProperty("Number_BadBeasts", "5"));
        NUMBER_OF_GP = Integer.parseInt(properties.getProperty("Number_GoodPlants", "20"));
        NUMBER_OF_BP = Integer.parseInt(properties.getProperty("Number_BadPlants", "6"));
        NUMBER_OF_WA = Integer.parseInt(properties.getProperty("Number_Walls", "10"));

        //POINTS_FOR_MINI_SQUIRREL = Integer.parseInt(properties.getProperty("Points_For_MiniSquirrel"));

        GAME_DURATION_AT_START = Long.parseLong(properties.getProperty("Game_Duration", "1800"));
        TICKLENGTH = Integer.parseInt(properties.getProperty("Ticklength", "10"));
        //SQUIRREL_STUN_TIME_IN_TICKS = Integer.parseInt(properties.getProperty("Squirrel_Stun_Time_Ticks"));
        properties.getProperty("Beast_Move_Time_Ticks", "4");
        properties.getProperty("MiniSquirrel_Move_Time_Ticks", "1");

        VIEW_DISTANCE_OF_GOODBEAST = Integer.parseInt(properties.getProperty("View_Distance_GoodBeast", "7"));
        VIEW_DISTANCE_OF_BADBEAST = Integer.parseInt(properties.getProperty("View_Distance_BadBeast", "7"));

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

    public int getTICKLENGTH() {
        return TICKLENGTH;
    }

    int getPOINTS_FOR_MINI_SQUIRREL() {
        return POINTS_FOR_MINI_SQUIRREL;
    }

    int getSQUIRREL_STUN_TIME_IN_TICKS() {
        return SQUIRREL_STUN_TIME_IN_TICKS;
    }

    int getBEAST_MOVE_TIME_IN_TICKS() {
        return BEAST_MOVE_TIME_IN_TICKS;
    }

    int getMINI_SQUIRREL_MOVE_TIME_IN_TICKS() {
        return MINI_SQUIRREL_MOVE_TIME_IN_TICKS;
    }

    int getVIEW_DISTANCE_OF_GOODBEAST() {
        return VIEW_DISTANCE_OF_GOODBEAST;
    }

    int getVIEW_DISTANCE_OF_BADBEAST() {
        return VIEW_DISTANCE_OF_BADBEAST;
    }

    int getNUMBER_OF_GB() {
        return NUMBER_OF_GB;
    }

    int getNUMBER_OF_BB() {
        return NUMBER_OF_BB;
    }

    int getNUMBER_OF_GP() {
        return NUMBER_OF_GP;
    }

    int getNUMBER_OF_BP() {
        return NUMBER_OF_BP;
    }

    int getNUMBER_OF_WA() {
        return NUMBER_OF_WA;
    }

    int getNUMBER_OF_BOTS() {
        return bots.length;
    }

    public long getGAME_DURATION_AT_START() {
        return GAME_DURATION_AT_START;
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

        File folder = new File("src\\de\\hsa\\games\\fatsquirrel\\botimpls");
        File[] listOfBots = folder.listFiles((dir, name) -> name.substring(name.length()-4, name.length()).equals("java"));
        String[] bots;

        if(listOfBots != null) {
            bots = new String[listOfBots.length];
            for (int i = 0; i < listOfBots.length; i++) {
                if (listOfBots[i].isFile()) {
                    // \\\\ = \   | Beim Regex muss ein Backslash mit drei Backslashes escaped werden
                    String s = listOfBots[i].toString().split("\\\\")[6];
                    bots[i] = s.substring(0, s.length()-5);
                }
            }
            return bots;
        }
        return null;
    }

}
