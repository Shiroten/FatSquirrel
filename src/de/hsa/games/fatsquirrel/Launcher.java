package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.console.GameImpl;
import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.State;
import de.hsa.games.fatsquirrel.gui.FxGameImpl;
import de.hsa.games.fatsquirrel.gui.FxUI;
import de.hsa.games.fatsquirrel.util.ui.consoletest.MyFavoriteCommandsProcessor;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.*;

public class Launcher extends Application {

    private static final int tickLength = 60;
    private static final Game.GameType gameType = Game.GameType.SINGLE_PLAYER;
    private static final Level logLevel = Level.FINER;
    private static final XY gameSize = new XY(40, 30);
    private static final int NUMBER_OF_GB = 500;
    private static final int NUMBER_OF_BB = 0;
    private static final int NUMBER_OF_GP = 5;
    private static final int NUMBER_OF_BP = 5;
    private static final int NUMBER_OF_WA = 0;
    private static final int NUMBER_OF_BOTS = 4;
    private static final int VIEW_DISTANCE_OF_GOODBEAST = 6;
    private static final int VIEW_DISTANCE_OF_BADBEAST = 6;
    private static int cellSize = 25;

    private static final defaultNumber dn = defaultNumber.normal;

    public enum defaultNumber {
        testcase1,
        testcase2,
        testcase3,
        testcase4,
        testcase5,
        GBbreeding,
        custom,
        normal,
    }

    public static void main(String[] args) {

        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.setLevel(logLevel);
        try {
            Handler handler = new FileHandler("log.txt");
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
            handler.setLevel(Level.FINE);
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int switchMode = 0;

        switch (switchMode) {
            case 0:
                Application.launch(args);
                break;
            case 1:
                //For SingleThreaded Console Game
                consoleTest();
                break;
            case 2:
                commandTest();
                break;
        }
    }

    private static void commandTest() {

        MyFavoriteCommandsProcessor myFavoriteCommandsProcessor = new MyFavoriteCommandsProcessor();
        myFavoriteCommandsProcessor.process();
    }

    private static void consoleTest() {
        Game game = new GameImpl();
        game.startSingleThreadGame();
    }

    private static void startGame(Game game) {
        game.setGameSpeed(game.getState().getBoard().getConfig().getTICKLENGTH());
        try {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Logger logger = Logger.getLogger(Launcher.class.getName());
                    logger.log(Level.FINEST, "start game.processInput()");
                    game.processInput();
                    try {
                        //System.out.println(game.getTickLength());
                        Thread.sleep(game.getTickLength());
                        game.run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 500, 1);
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();

        }

    }

    @Override
    public void start(Stage primaryStage) {
        int multiplier;
        int density;
        BoardConfig config;
        switch (dn) {

            case normal:
                multiplier = 5;
                config = new BoardConfig(new XY(16 * multiplier, 9 * multiplier), 100,
                        50, 7, 7, 7, 50,
                        NUMBER_OF_BOTS, 7, 7, Game.GameType.WITH_BOT);
                cellSize = 10;
                break;
            case testcase1:
                config = new BoardConfig(new XY(30, 30), 100,
                        500, 0, 0, 0, 0,
                        0, 20, 5, Game.GameType.SINGLE_PLAYER);
                break;
            case testcase2:
                config = new BoardConfig(new XY(30, 30), 100,
                        100, 15, 0, 0, 50,
                        NUMBER_OF_BOTS, 20, 20, Game.GameType.SINGLE_PLAYER);
                break;
            case testcase3:
                config = new BoardConfig(new XY(30, 30), 40,
                        50, 0, 0, 0, 100,
                        NUMBER_OF_BOTS, 20, 20, Game.GameType.WITH_BOT);
                break;
            case testcase4:
                config = new BoardConfig(new XY(10, 10), 40,
                        40, 0, 0, 0, 0,
                        0, 20, 20, Game.GameType.SINGLE_PLAYER);
                break;
            case testcase5:
                multiplier = 10;
                density = 14*8*multiplier;
                config = new BoardConfig(new XY(14 * multiplier, 8 * multiplier), 40,
                        density/32, density/64, density/64, density/64, density/8,
                        NUMBER_OF_BOTS, 7, 7, Game.GameType.WITH_BOT);
                break;
            case GBbreeding:
                config = new BoardConfig(new XY(10, 10), 20,
                        2, 0, 0, 0, 0,
                        0, 20, 20, Game.GameType.SINGLE_PLAYER);

                break;
            case custom:
            default:
                config = new BoardConfig(gameSize, tickLength,
                        NUMBER_OF_GB, NUMBER_OF_BB, NUMBER_OF_GP, NUMBER_OF_BP, NUMBER_OF_WA,
                        NUMBER_OF_BOTS, VIEW_DISTANCE_OF_GOODBEAST, VIEW_DISTANCE_OF_BADBEAST, gameType);

        }
        Board board = new Board(config);
        State state = new State(board);
        FxUI fxUI = FxUI.createInstance(state.getBoard().getConfig().getSize(), cellSize);
        final Game game = new FxGameImpl(fxUI, state);

        primaryStage.setScene(fxUI);
        primaryStage.setTitle("Diligent Squirrel");
        primaryStage.setHeight(600);
        primaryStage.setWidth(1000);

        fxUI.getWindow().setOnCloseRequest(evt -> System.exit(-1));

        game.render();
        primaryStage.show();

        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.INFO, "Starting Game");
        startGame(game);
    }
}