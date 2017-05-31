package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.XY;

public class Settings {

    //TODO: Settings gänzlich eleminieren
    public static final int tickLength = 100;
    public static final Game.GameType gameType = Game.GameType.SINGLE_PLAYER;
    public static final XY gameSize = new XY(40, 30);
    public static final int NUMBER_OF_GB = 500;
    public static final int NUMBER_OF_BB = 0;
    public static final int NUMBER_OF_GP = 5;
    public static final int NUMBER_OF_BP = 5;
    public static final int NUMBER_OF_WA = 0;
    public static final int NUMBER_OF_BOTS = 4;
    public static final int VIEW_DISTANCE_OF_GOODBEAST = 6;
    public static final int VIEW_DISTANCE_OF_BADBEAST = 6;
    public static int cellSize = 25;
    public static final int GAME_DURATION = 1800;
    public static final defaultSettings ds = defaultSettings.normal;

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

    public static BoardConfig getDefaultBoardConfig() {
        int multiplier;
        int density;
        BoardConfig config;

        switch (ds) {

            case normal:
                multiplier = 5;
                config = new BoardConfig("default.props");/*new BoardConfig(new XY(16 * multiplier, 9 * multiplier), 100,
                        50, 7, 7, 7, 50,
                        NUMBER_OF_BOTS, 7, 7, Game.GameType.WITH_BOT, 1800);*/
                cellSize = 10;
                break;
            case testcase1:
                config = new BoardConfig(new XY(30, 30), 100,
                        500, 0, 0, 0, 0,
                        0, 20, 5, Game.GameType.SINGLE_PLAYER, 1800);
                break;
            case testcase2:
                config = new BoardConfig(new XY(30, 30), 100,
                        100, 15, 0, 0, 50,
                        NUMBER_OF_BOTS, 20, 20, Game.GameType.SINGLE_PLAYER, 1800);
                break;
            case testcase3:
                config = new BoardConfig(new XY(30, 30), 40,
                        50, 0, 0, 0, 100,
                        NUMBER_OF_BOTS, 20, 20, Game.GameType.WITH_BOT, 1800);
                break;
            case testcase4:
                config = new BoardConfig(new XY(10, 10), 40,
                        40, 0, 0, 0, 0,
                        0, 20, 20, Game.GameType.SINGLE_PLAYER, 1800);
                break;
            case testcase5:
                multiplier = 10;
                density = 14 * 8 * multiplier;
                config = new BoardConfig(new XY(14 * multiplier, 8 * multiplier), 40,
                        density / 32, density / 64, density / 64, density / 64, density / 8,
                        NUMBER_OF_BOTS, 7, 7, Game.GameType.WITH_BOT, 1800);
                break;
            case GBbreeding:
                config = new BoardConfig(new XY(10, 10), 20,
                        2, 0, 0, 0, 0,
                        0, 20, 20, Game.GameType.SINGLE_PLAYER, 1800);
                break;
            //TODO: In file schieben
            case custom:
            default:
                config = new BoardConfig(gameSize, tickLength,
                        NUMBER_OF_GB, NUMBER_OF_BB, NUMBER_OF_GP,
                        NUMBER_OF_BP, NUMBER_OF_WA, NUMBER_OF_BOTS,
                        VIEW_DISTANCE_OF_GOODBEAST, VIEW_DISTANCE_OF_BADBEAST,
                        gameType, GAME_DURATION);
        }
        return config;
    }


}
