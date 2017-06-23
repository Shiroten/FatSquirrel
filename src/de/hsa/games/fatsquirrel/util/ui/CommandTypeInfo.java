package de.hsa.games.fatsquirrel.util.ui;

public interface CommandTypeInfo {
    String getName();
    String getHelpText();
    Class<?>[] getParamTypes();
}
