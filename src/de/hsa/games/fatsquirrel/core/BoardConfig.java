package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;

import java.io.*;
import java.util.Properties;

public class BoardConfig {

    private final XY size;

    private final int NUMBER_OF_GB;
    private final int NUMBER_OF_BB;
    private final int NUMBER_OF_GP;
    private final int NUMBER_OF_BP;
    private final int NUMBER_OF_WA;

    private final int NUMBER_OF_BOTS;

    private final int POINTS_FOR_MINI_SQUIRREL = 150;

    private final long GAME_DURATION_AT_START;
    private final int TICKLENGTH;
    //private final float SQUIRREL_STUN_TIME_LENGTH = 0.2f;
    private final int SQUIRREL_STUN_TIME_IN_TICKS = 3;
    //private final float BEAST_MOVE_TIME_LENGTH = 0.1f;
    private final int BEAST_MOVE_TIME_IN_TICKS = 4;
    //private final float MINI_SQUIRREL_MOVE_TIME_LENGTH = 0.07f;
    private final int MINI_SQUIRREL_MOVE_TIME_IN_TICKS = 1;

    private final int VIEW_DISTANCE_OF_GOODBEAST;
    private final int VIEW_DISTANCE_OF_BADBEAST;

    private final Game.GameType gameType;
    private final String[] bots = {"BasterFactory", "GoodBeastChaserFactory"};

    public static final defaultSettings ds = defaultSettings.normal;

    public BoardConfig(XY size, int tickLength, int gb, int bb, int gp, int bp, int wa, int NUMBER_OF_BOTS
            , int viewGB, int viewBB, Game.GameType gameType, int gameDuration) {
        this.size = size;
        this.TICKLENGTH = tickLength;
        this.NUMBER_OF_GB = gb;
        this.NUMBER_OF_BB = bb;
        this.NUMBER_OF_GP = gp;
        this.NUMBER_OF_BP = bp;
        this.NUMBER_OF_WA = wa;
        //this.SQUIRREL_STUN_TIME_IN_TICKS = (int) (tickLength * SQUIRREL_STUN_TIME_LENGTH);
        //this.BEAST_MOVE_TIME_IN_TICKS = (int) (tickLength * BEAST_MOVE_TIME_LENGTH);
        //this.MINI_SQUIRREL_MOVE_TIME_IN_TICKS = (int) (tickLength * MINI_SQUIRREL_MOVE_TIME_LENGTH);
        this.NUMBER_OF_BOTS = NUMBER_OF_BOTS;
        this.VIEW_DISTANCE_OF_GOODBEAST = viewGB;
        this.VIEW_DISTANCE_OF_BADBEAST = viewBB;
        this.gameType = gameType;
        this.GAME_DURATION_AT_START = gameDuration;

        saveConfig();
    }

    public BoardConfig() {
        this.size = new XY (40, 30);
        this.TICKLENGTH = 60;
        this.NUMBER_OF_GB = 50;
        this.NUMBER_OF_BB = 5;
        this.NUMBER_OF_GP = 5;
        this.NUMBER_OF_BP = 5;
        this.NUMBER_OF_WA = 25;
        this.NUMBER_OF_BOTS = 4;
        //this.SQUIRREL_STUN_TIME_IN_TICKS = (int) (TICKLENGTH * SQUIRREL_STUN_TIME_LENGTH);
        //this.BEAST_MOVE_TIME_IN_TICKS = (int) (TICKLENGTH * BEAST_MOVE_TIME_LENGTH);
        //this.MINI_SQUIRREL_MOVE_TIME_IN_TICKS = (int) (TICKLENGTH * MINI_SQUIRREL_MOVE_TIME_LENGTH);
        this.VIEW_DISTANCE_OF_GOODBEAST = 7;
        this.VIEW_DISTANCE_OF_BADBEAST = 6;
        this.gameType = Game.GameType.WITH_BOT;
        this.GAME_DURATION_AT_START = 300;

        saveConfig();
    }

    public BoardConfig(XY size) {
        this.size = size;
        this.TICKLENGTH = 60;
        this.NUMBER_OF_GB = 50;
        this.NUMBER_OF_BB = 5;
        this.NUMBER_OF_GP = 5;
        this.NUMBER_OF_BP = 5;
        this.NUMBER_OF_WA = 25;
        this.NUMBER_OF_BOTS = 4;
        //this.SQUIRREL_STUN_TIME_IN_TICKS = (int) (TICKLENGTH * SQUIRREL_STUN_TIME_LENGTH);
        //this.BEAST_MOVE_TIME_IN_TICKS = (int) (TICKLENGTH * BEAST_MOVE_TIME_LENGTH);
        //this.MINI_SQUIRREL_MOVE_TIME_IN_TICKS = (int) (TICKLENGTH * MINI_SQUIRREL_MOVE_TIME_LENGTH);
        this.VIEW_DISTANCE_OF_GOODBEAST = 7;
        this.VIEW_DISTANCE_OF_BADBEAST = 6;
        this.gameType = Game.GameType.WITH_BOT;
        this.GAME_DURATION_AT_START = 1800;

        saveConfig();
    }

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

    //TODO: Dies den einzigen Konstruktor machen
    public BoardConfig(String filename){
        Properties properties = new Properties();
        try {
            Reader reader = new FileReader(filename);
            properties.load(reader);
            properties.getProperty("GameMode");
        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println(properties.getProperty("Size"));
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

        NUMBER_OF_BOTS = 2;
    }

    public BoardConfig(XY size, int NUMBER_OF_GB, int NUMBER_OF_BB, int NUMBER_OF_GP, int NUMBER_OF_BP, int NUMBER_OF_WA) {
        this(size, 60, NUMBER_OF_GB, NUMBER_OF_BB, NUMBER_OF_GP, NUMBER_OF_BP, NUMBER_OF_WA, 4, 6, 6, Game.GameType.SINGLE_PLAYER,1800);
    }

    public enum defaultSettings {
        testcase1,
        testcase2,
        testcase3,
        testcase4,
        testcase5,
        GBbreeding,
        custom,
        normal,
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

    int getGOODBEAST_VIEW_DISTANCE() {
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

    String[] getBots(){
        System.out.println("Aufruf");
        File folder = new File("src\\de\\hsa\\games\\fatsquirrel\\botimpls");
        //C:\Users\tillm\IdeaProjects\FatSquirrel\src\
        File[] listOfBots = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.substring(name.length()-4, name.length()).equals("java");
            }
        });
        String[] bots;

        if(listOfBots != null) {
            bots = new String[listOfBots.length];
            for (int i = 0; i < listOfBots.length; i++) {
                if (listOfBots[i].isFile()) {
                    //System.out.println(listOfBots[i].toString());
                    String s = listOfBots[i].toString().split("\\\\")[6];
                    System.out.println(s.substring(0, s.length()-5));
                    bots[i] = s.substring(0, s.length()-5);
                }
            }

            return bots;
        }

        return null;
    }

}
