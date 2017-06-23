package de.hsa.games.fatsquirrel.util.ui;

import java.io.BufferedReader;
import java.io.IOException;

public class CommandScanner {
    private final CommandTypeInfo[] commandTypeInfos;
    private final BufferedReader inputReader;

    public CommandScanner(CommandTypeInfo[] commandTypeInfos, BufferedReader inputReader) {
        this.commandTypeInfos = commandTypeInfos;
        this.inputReader = inputReader;
    }

    public Command next() throws ScanException {
        CommandTypeInfo commandType = null;
        Object[] params = null;
        System.out.println("Geben Sie Ihren Befehl ein: ");
        try {
            String input = inputReader.readLine();
            String[] inputSplit = input.split(" ");
            for (CommandTypeInfo i : commandTypeInfos) {
                if (i.getName().equals(inputSplit[0])) {
                    commandType = i;
                    if(i.getParamTypes() != null)
                        params = new Object[i.getParamTypes().length];
                }
            }
            if (commandType == null)
                throw new ScanException("Befehl nicht erkannt");
            if(commandType.getParamTypes() != null) {
                for (int i = 0; i < params.length; i++) {
                    try {
                        switch (commandType.getParamTypes()[i].getCanonicalName()) {
                            case "int":
                                params[i] = Integer.parseInt(inputSplit[i+1]);
                                break;
                            case "java.lang.String":
                                params[i] = inputSplit[i+1];
                                break;
                            case "float":
                                params[i] = Float.parseFloat(inputSplit[i+1]);
                                break;
                            default:
                                throw new ScanException("Parameter falsch zugewiesen");
                        }
                    } catch (Exception e) {
                        throw new ScanException("Parameter nicht richtig zugewiesen");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Command(commandType, params);
    }

}
