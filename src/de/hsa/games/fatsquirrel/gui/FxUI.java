package de.hsa.games.fatsquirrel.gui;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.UI;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.console.GameCommandType;
import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.core.entity.Entity;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.character.BadBeast;
import de.hsa.games.fatsquirrel.core.entity.character.Character;
import de.hsa.games.fatsquirrel.core.entity.character.PlayerEntity;
import de.hsa.games.fatsquirrel.util.ui.Command;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FxUI extends Scene implements UI {

    private Canvas boardCanvas;
    private Label msgLabel;
    private static Command cmd = new Command(GameCommandType.NOTHING, new Object[0]);
    private static outputLevel outputMode = outputLevel.simple;
    private static showLastVector printVector = showLastVector.tail;
    private static toogleInput inputMode = toogleInput.inputEnergy;
    private static boolean showName = false;
    private static int CELL_SIZE_AT_START;
    private static int cellSize = CELL_SIZE_AT_START;
    private static int miniSquirrelEnergy = 200;
    private static int miniSquirrelRadius = 5;
    private static int fieldSizeMax;
    private static int fieldSizeMin;
    private static int fieldSizeX;
    private static int fieldSizeY;
    private static double scaleFactor = (double) cellSize / CELL_SIZE_AT_START;


    public enum toogleInput {
        inputEnergy,
        inputRadius,
    }

    public enum showLastVector {
        none,
        head,
        tail,
        headAndTail,
    }

    public enum outputLevel {
        simple,
        detailed,
        extended,
        showID,
    }

    private FxUI(Parent parent, Canvas boardCanvas, Label msgLabel, int cellSize) {
        super(parent);
        this.boardCanvas = boardCanvas;
        this.msgLabel = msgLabel;
        this.CELL_SIZE_AT_START = cellSize;
    }

    public static FxUI createInstance(XY boardSize, int cellSize) {
        fieldSizeX = boardSize.getX();
        fieldSizeY = boardSize.getY();

        Canvas boardCanvas = new Canvas(boardSize.getX() * FxUI.cellSize, boardSize.getY() * FxUI.cellSize);
        Label statusLabel = new Label();
        VBox top = new VBox();
        top.getChildren().add(boardCanvas);
        top.getChildren().add(statusLabel);

        statusLabel.setText("Fange Squirrel ein");
        final FxUI fxUI = new FxUI(top, boardCanvas, statusLabel, cellSize);

        fxUI.setOnKeyPressed(
                keyEvent -> {
                    System.out.println(keyEvent.getCode());
                    switch (keyEvent.getCode()) {
                        case W:
                        case UP:
                            cmd = new Command(GameCommandType.UP, new Object[0]);
                            break;
                        case A:
                        case LEFT:
                            cmd = new Command(GameCommandType.LEFT, new Object[0]);
                            break;
                        case S:
                        case DOWN:
                            cmd = new Command(GameCommandType.DOWN, new Object[0]);
                            break;
                        case D:
                        case RIGHT:
                            cmd = new Command(GameCommandType.RIGHT, new Object[0]);
                            break;
                        case F:
                            //Todo: Spawn Mini Energy in Config setzen oder per menü
                            cmd = new Command(GameCommandType.SPAWN_MINI,
                                    new Object[]{miniSquirrelEnergy});
                            break;
                        case INSERT:
                            cmd = new Command(GameCommandType.CHEAT_ENERGY, new Object[0]);
                            break;
                        case T:
                            cmd = new Command(GameCommandType.IMPLODE_MINISQUIRRELS, new Object[]{miniSquirrelRadius});
                            break;
                        case B:
                            switch (printVector) {
                                case none:
                                    printVector = showLastVector.headAndTail;
                                    break;
                                case head:
                                    printVector = showLastVector.tail;
                                    break;
                                case tail:
                                    printVector = showLastVector.none;
                                    break;
                                case headAndTail:
                                    printVector = showLastVector.head;
                                    break;
                            }
                            break;
                        case V:
                            switch (outputMode) {
                                case simple:
                                    outputMode = outputLevel.detailed;
                                    break;
                                case detailed:
                                    outputMode = outputLevel.extended;
                                    break;
                                case extended:
                                    outputMode = outputLevel.showID;
                                    break;
                                case showID:
                                    outputMode = outputLevel.simple;
                            }
                            break;
                        case C:
                            showName = !showName;
                            break;
                        case NUMPAD0:
                        case N:
                            calcInput(0);
                            break;
                        case NUMPAD1:
                        case M:
                            calcInput(1);
                            break;
                        case NUMPAD2:
                        case COMMA:
                            calcInput(2);
                            break;
                        case NUMPAD3:
                        case PERIOD:
                            calcInput(3);
                            break;
                        case NUMPAD4:
                        case J:
                            calcInput(4);
                            break;
                        case NUMPAD5:
                        case K:
                            calcInput(5);
                            break;
                        case NUMPAD6:
                        case L:
                            calcInput(6);
                            break;
                        case NUMPAD7:
                        case U:
                            calcInput(7);
                            break;
                        case NUMPAD8:
                        case I:
                            calcInput(8);
                            break;
                        case NUMPAD9:
                        case O:
                            calcInput(9);
                            break;
                        case ADD:
                        case P:
                            miniSquirrelEnergy = 0;
                            break;
                        case SUBTRACT:
                        case UNDEFINED:
                            switch (inputMode) {
                                case inputEnergy:
                                    inputMode = toogleInput.inputRadius;
                                    break;
                                case inputRadius:
                                    inputMode = toogleInput.inputEnergy;
                                    break;
                            }
                            break;
                        case PLUS:
                            cmd = new Command(GameCommandType.ADD_GAME_SPEED, new Object[0]);
                            break;
                        case NUMBER_SIGN:
                            cmd = new Command(GameCommandType.SUBTRACT_GAME_SPEED, new Object[0]);
                            break;
                        default:
                            cmd = new Command(GameCommandType.NOTHING, new Object[0]);
                    }
                }
        );
        return fxUI;
    }

    @Override
    public void render(final BoardView view) {
        Platform.runLater(() -> repaintBoardCanvas(view));
    }

    private void repaintBoardCanvas(BoardView view) {
        scaleFactor = (double) cellSize / CELL_SIZE_AT_START;

        double xSize = this.getWidth();
        double ySize = this.getHeight() - 17;
        double Size = xSize < ySize ? xSize : ySize;
        fieldSizeMax = xSize < ySize ? fieldSizeX : fieldSizeY;

        cellSize = (int) (Size / fieldSizeMax);
        int fontSize = (int) (cellSize * 18.0 / 40.0);
        boardCanvas.setHeight(fieldSizeY * cellSize);
        boardCanvas.setWidth(fieldSizeX * cellSize);

        GraphicsContext gc = boardCanvas.getGraphicsContext2D();

        gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());
        gc.setFont(Font.font("Courier", fontSize));

        try {
            printImplosion(gc, view);

            printEntity(gc, view);
            printHeadOrTail(gc, view);

        } catch (Exception e) {
            Logger logger = Logger.getLogger(Launcher.class.getName());
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private void printEntity(GraphicsContext gc, BoardView view) {

        for (int x = 0; x < boardCanvas.getWidth(); x++) {
            for (int y = 0; y < boardCanvas.getHeight(); y++) {
                if (view.getEntity(new XY(x, y)) == null)
                    continue;
                printEntity(gc, view.getEntity(new XY(x, y)), new XY(x, y));
            }
        }
    }

    private void printHeadOrTail(GraphicsContext gc, BoardView view) {

        for (int x = 0; x < boardCanvas.getWidth(); x++) {
            for (int y = 0; y < boardCanvas.getHeight(); y++) {
                if (view.getEntity(new XY(x, y)) == null)
                    continue;
                if (printVector == showLastVector.none)
                    continue;
                EntityType et = view.getEntity(new XY(x, y)).getEntityType();
                switch (et) {
                    case WALL:
                    case NONE:
                    case GOODPLANT:
                    case BADPLANT:
                        break;
                    default:
                        XY lastVector = ((Character) view.getEntity(new XY(x, y))).getLastVector();
                        if (lastVector.equals(XY.ZERO_ZERO)) {
                        } else if (lastVector.equals(XY.RIGHT_UP)) {
                            printVector(gc, x, y, 4);
                        } else if (lastVector.equals(XY.RIGHT)) {
                            printVector(gc, x, y, 5);
                        } else if (lastVector.equals(XY.RIGHT_DOWN)) {
                            printVector(gc, x, y, 6);
                        } else if (lastVector.equals(XY.DOWN)) {
                            printVector(gc, x, y, 7);
                        } else if (lastVector.equals(XY.LEFT_DOWN)) {
                            printVector(gc, x, y, 0);
                        } else if (lastVector.equals(XY.LEFT)) {
                            printVector(gc, x, y, 1);
                        } else if (lastVector.equals(XY.LEFT_UP)) {
                            printVector(gc, x, y, 2);
                        } else if (lastVector.equals(XY.UP)) {
                            printVector(gc, x, y, 3);
                        }
                }
            }
        }

    }

    private void printImplosion(GraphicsContext gc, BoardView view) throws Exception {
        for (ImplosionContext ic : view.getImplosions()) {

            double opacity = (((double) ic.getTickCounter() / ic.getMAX_TICK_COUNTER()));
            if (opacity < 0)
                opacity = 0;

            Color implisionColor = Color.color(1, 0, 0, opacity);

            gc.setFill(implisionColor);
            gc.fillOval(ic.getPosition().getX() * cellSize - cellSize * ic.getRadius() + (cellSize / 2),
                    ic.getPosition().getY() * cellSize - cellSize * ic.getRadius() + (cellSize / 2),
                    cellSize * ic.getRadius() * 2,
                    cellSize * ic.getRadius() * 2);

            if (outputMode == outputLevel.extended) {
                gc.setFill(Color.BLACK);
                gc.fillText(Integer.toString(ic.getTickCounter()),
                        ic.getPosition().getX() * cellSize + (cellSize / 2),
                        ic.getPosition().getY() * cellSize + (cellSize / 2));
            }
        }
    }

    private void printVector(GraphicsContext gc, int x, int y, int numberOfRotation) {

        double rotationCenterX = (x * cellSize + cellSize / 2);
        double rotationCenterY = (y * cellSize + cellSize / 2);

        gc.save();
        gc.translate(rotationCenterX, rotationCenterY);

        int rotateOffset = 45;
        for (int i = 0; i < numberOfRotation; i++) {
            rotateOffset += 45;
        }

        gc.rotate(45 + rotateOffset);
        //Head
        if (printVector == showLastVector.head || printVector == showLastVector.headAndTail) {
            gc.setFill(Color.color(0.702, 0.3098, 0.0824));
            double offset = -cellSize / 2;
            gc.fillRect(offset, offset, 1 * scaleFactor, 5 * scaleFactor);
            gc.fillRect(offset, offset, 5 * scaleFactor, 1 * scaleFactor);
        }

        gc.rotate(0);
        //Tail
        if (printVector == showLastVector.tail || printVector == showLastVector.headAndTail) {
            gc.setFill(Color.color(0.4275, 0.1961, 0.0431));
            double offsetTail = cellSize / 2 - 4 * scaleFactor;
            gc.fillRect(offsetTail, offsetTail, 1.5 * scaleFactor, 6.5 * scaleFactor);
            gc.fillRect(offsetTail, offsetTail, 6.5 * scaleFactor, 1.5 * scaleFactor);
            gc.fillRect(offsetTail, offsetTail, 3.5 * scaleFactor, 3.5 * scaleFactor);
        }
        gc.restore();
    }

    private void printEntity(GraphicsContext gc, Entity e, XY xy) {

        if (e == null)
            return;

        //Kästchen für die Entity setzen
        EntityType et = e.getEntityType();
        gc.setFill(entityTypeToColor(e));

        switch (et) {
            case WALL:
                gc.fillRect(xy.getX() * cellSize, xy.getY() * cellSize, cellSize, cellSize);
                break;
            case NONE:
                return;
            default:
                gc.fillOval(xy.getX() * cellSize, xy.getY() * cellSize, cellSize, cellSize);
        }

        //Text Schreiben
        gc.setFill(entityTypeToTextColor(e));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(entityToString(e), (xy.getX() + 0.5) * cellSize, (xy.getY() + 0.5) * cellSize);
    }

    private Color entityTypeToColor(Entity e) {
        EntityType et = e.getEntityType();
        Color returnColor;

        switch (et) {
            case GOODPLANT:
                returnColor = Color.color(0, 1, 0);
                break;
            case GOODBEAST:
                returnColor = Color.color(1, 0.9765, 0);
                break;
            case BADPLANT:
                returnColor = Color.color(0, 0.2353, 0);
                break;
            case BADBEAST:
                returnColor = Color.color(1, 0.0392, 0);
                break;
            case WALL:
                returnColor = Color.color(0.3804, 0.3804, 0.3765);
                break;
            case MINISQUIRREL:
                returnColor = Color.color(1, 0.5412, 0);
                break;
            case MASTERSQUIRREL:
                if (e.getId() == -100) {
                    returnColor = Color.color(0, 0.9608, 1);
                } else {
                    returnColor = Color.color(0, 0.0588, 1);
                }

                break;
            default:
                returnColor = Color.gray(0, 0);
        }
        return returnColor;
    }

    private Color entityTypeToTextColor(Entity e) {

        Color returnColor = Color.BLACK;
        EntityType et = e.getEntityType();

        switch (et) {
            case GOODPLANT:

                break;
            case GOODBEAST:

                break;
            case BADPLANT:
                returnColor = Color.WHITE;
                break;
            case BADBEAST:

                break;
            case WALL:

                break;
            case MINISQUIRREL:
                if (((PlayerEntity) e).getStunTime() != 0) {
                    returnColor = Color.RED;
                }

                break;
            case MASTERSQUIRREL:
                if (((PlayerEntity) e).getStunTime() != 0) {
                    returnColor = Color.RED;
                } else {
                    if (e.getId() == -100) {
                        returnColor = Color.BLACK;
                    } else {
                        returnColor = Color.color(1, 0.651, 0);
                    }
                }
                break;
            default:
                returnColor = Color.BLACK;
        }
        return returnColor;
    }

    private String entityToString(Entity e) {

        EntityType et = e.getEntityType();
        String stringToPrint;
        String simpleText, detailedText, extendText, showIDText = Integer.toString(e.getId());

        switch (et) {
            case GOODPLANT:
                simpleText = detailedText = "GP";
                extendText = Integer.toString(e.getEnergy());
                break;
            case GOODBEAST:
                simpleText = detailedText = "GB";
                extendText = Integer.toString(e.getEnergy());
                break;
            case BADPLANT:
                simpleText = detailedText = "BP";
                extendText = Integer.toString(e.getEnergy());
                break;
            case BADBEAST:
                simpleText = "BB";
                detailedText = Integer.toString(e.getEnergy());
                extendText = Integer.toString(((BadBeast) e).getLives());
                break;
            case WALL:
                simpleText = detailedText = extendText = "";
                break;
            case MINISQUIRREL:
                simpleText = "mS";
                detailedText = Integer.toString(e.getEnergy());
                //detailedText = String.format("A%n" + detailedText);
                if (((PlayerEntity) e).getStunTime() != 0) {
                    extendText = Integer.toString(((PlayerEntity) e).getStunTime());
                    //extendText = String.format("A%n" + extendText);
                } else {
                    extendText = detailedText;
                }
                break;
            case MASTERSQUIRREL:
                if (e.getId() == -100) {
                    simpleText = "HS";
                } else {
                    simpleText = "MS";
                }

                detailedText = Integer.toString(e.getEnergy());
                //detailedText = String.format("A%n" + detailedText);
                if (((PlayerEntity) e).getStunTime() != 0) {
                    extendText = Integer.toString(((PlayerEntity) e).getStunTime());
                    //extendText = String.format("A%n" + extendText);
                } else {
                    extendText = detailedText;
                }
                break;
            default:
                simpleText = detailedText = extendText = "";
        }

        if (showName && e.getEntityName() != null)
            simpleText = detailedText = extendText = showIDText = e.getEntityName();
        else if (showName)
            detailedText = extendText = showIDText = simpleText;
        else
            ;

        stringToPrint = switchVerboseLevel(outputMode, simpleText, detailedText, extendText, showIDText);

        return stringToPrint;
    }

    private String switchVerboseLevel(outputLevel vl, String simpleText, String detailedText,
                                      String extendedText, String showIDText) {

        String stringToPrint = simpleText;

        switch (vl) {
            case simple:
                stringToPrint = simpleText;
                break;
            case detailed:
                stringToPrint = detailedText;
                break;
            case extended:
                stringToPrint = extendedText;
                break;
            case showID:
                stringToPrint = showIDText;

        }

        return stringToPrint;
    }

    void message(final String msg) {

        String addmsg;
        if (inputMode == toogleInput.inputEnergy) {
            addmsg = String.format(" | MiniSquirrel-InputEnergy: " + miniSquirrelEnergy);
        } else if (inputMode == toogleInput.inputRadius) {
            addmsg = String.format(" | MiniSquirrel-InputRadius: " + miniSquirrelRadius);
        } else {
            addmsg = "";
        }

        final String outmsg = msg + addmsg;
        Platform.runLater(() -> msgLabel.setText(outmsg));
    }

    @Override
    public Command getCommand() {
        Command temp = cmd;
        cmd = new Command(GameCommandType.NOTHING, new Object[0]);
        return temp;

    }

    private static void calcInput(int input) {
        if (inputMode == toogleInput.inputEnergy) {
            miniSquirrelEnergy = miniSquirrelEnergy * 10 + input;
        } else if (inputMode == toogleInput.inputRadius) {
            miniSquirrelRadius = input;
        }
    }

}