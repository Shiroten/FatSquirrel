package de.hsa.games.fatsquirrel.console;

import de.hsa.games.fatsquirrel.UI;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.util.ui.Command;
import de.hsa.games.fatsquirrel.util.ui.CommandScanner;
import de.hsa.games.fatsquirrel.util.ui.CommandTypeInfo;
import de.hsa.games.fatsquirrel.util.ui.ScanException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ConsoleUI implements UI {

    private PrintStream outputStream = new PrintStream(System.out);
    private BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
    private CommandScanner commandScanner = new CommandScanner(GameCommandType.values(), inputReader);

    @Override
    public Command getCommand() {

        try {
            return commandScanner.next();
        } catch (ScanException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void help() {
        for (CommandTypeInfo i : GameCommandType.values()) {
            outputStream.println(i.getName() + " " + i.getHelpText());
        }
    }

    @Override
    public void render(BoardView view) {

        System.out.printf("----:Aktuelles Board:----%n%n");
        //i =: Zeilen
        for (int i = 0; i < view.getSize().getY(); i++) {

            printBorder(view.getSize().getX());
            //j =: Spalten
            System.out.printf("|");
            for (int j = 0; j < view.getSize().getX(); j++) {
                //Schreiben der Einzelnen Felder
                System.out.printf("%s", printField(view, j, i));
            }
            //abschließen der Zeile
            System.out.println();
        }
        //Abschluss Linie
        printBorder(view.getSize().getX());

        //Einrücken
        System.out.printf("%n%n");


    }

    private void printBorder(int ammount) {
        System.out.printf("|");
        for (int j = 0; j < ammount; j++) {
            System.out.printf("----|");
        }
        System.out.printf("%n");
    }

    private String printField(BoardView view, int x, int y) {

        EntityType onField;
        if (view.getEntityType(new XY(x, y)) != null) {
            onField = view.getEntityType(new XY(x, y));
        } else {
            onField = EntityType.NONE;
        }

        String stringToPrint;

        switch (onField) {
            case GOODPLANT:
                stringToPrint = " GP |";
                break;
            case GOODBEAST:
                stringToPrint = " GB |";
                break;
            case BADPLANT:
                stringToPrint = " BP |";
                break;
            case BADBEAST:
                stringToPrint = " BB |";
                break;
            case WALL:
                stringToPrint = " WA |";
                break;
            case MINISQUIRREL:
                stringToPrint = " mS |";
                break;
            case MASTERSQUIRREL:
                stringToPrint = " MS |";
                break;
            default:
                stringToPrint = " .. |";

        }
        return stringToPrint;
    }
}
