package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.console.GameImpl;
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

    public static final Level logLevel = Level.FINER;

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
        game.getState().loadHighScore("HighScore.props");
        System.out.println(game.getState().printHighscore());
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
                        Thread.sleep(game.getTickLength());
                        if (game.getState().getBoard().getRemainingGameTime() != 0) {
                            game.run();
                            game.getState().getBoard().reduceRemainingGameTime();
                        } else {
                            game.run();
                            game.getState().updateHighscore();
                            game.getState().saveHighScore("HighScore.props");
                            System.out.println(game.getState().printHighscore());
                            Thread.sleep(1000 * 3);
                            game.reset();
                        }

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

        State state = new State("test.props");

        FxUI fxUI = FxUI.createInstance(state.getBoard().getConfig().getSize(), state.getBoard().getConfig().getSELLSIZE());
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