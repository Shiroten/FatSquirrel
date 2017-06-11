package de.hsa.games.fatsquirrel.util.ui.consoletest;

import de.hsa.games.fatsquirrel.util.ui.Command;
import de.hsa.games.fatsquirrel.util.ui.CommandScanner;
import de.hsa.games.fatsquirrel.util.ui.CommandTypeInfo;
import de.hsa.games.fatsquirrel.util.ui.ScanException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class MyFavoriteCommandsProcessor {

    private PrintStream outputStream = new PrintStream(System.out);
    private BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
    private CommandScanner commandScanner = new CommandScanner(MyFavoriteCommandType.values(), inputReader);


    public MyFavoriteCommandsProcessor() {
    }

    public static void main(String args[]){
        MyFavoriteCommandsProcessor processor = new MyFavoriteCommandsProcessor();
        processor.process();
    }

    public void process() {
        while (true) {
            //the loop over all commands with one input line for every command
            Command command;
            try {
                command = commandScanner.next();

                Object[] params = command.getParams();
                MyFavoriteCommandType commandType = (MyFavoriteCommandType) command.getCommandTypeInfo();

                switch (commandType) {
                    case EXIT:
                        System.exit(0);
                    case HELP:
                        help();
                        break;
                    case ADDI:
                        addi((int) params[0], (int) params[1]);
                        break;
                    case ADDF:
                        addf((float) params[0], (float) params[1]);
                        break;
                    case ECHO:
                        echo((String) params[0], (int) params[1]);
                        break;
                }

            } catch (ScanException e){
                System.out.println(e.getMessage());
                help();
            }

        }
    }

    private void help(){
        for(CommandTypeInfo i : MyFavoriteCommandType.values()){
            outputStream.println(i.getName() + " " + i.getHelpText());
        }
    }

    private void addi(int i, int j){
        outputStream.println(i + " + " + j + " = " + (i+j));
    }

    private void addf(float i, float j){
        outputStream.println(i + " + " + j + " = " + (i+j));
    }

    private void echo(String string, int times){
        for(int i = 0; i < times; i++){
            outputStream.println(string);
        }
    }
}
