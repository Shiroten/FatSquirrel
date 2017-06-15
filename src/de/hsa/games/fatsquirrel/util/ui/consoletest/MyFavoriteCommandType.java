package de.hsa.games.fatsquirrel.util.ui.consoletest;

import de.hsa.games.fatsquirrel.util.ui.CommandTypeInfo;

public enum MyFavoriteCommandType implements CommandTypeInfo {
    HELP("help", "  * list all commands"),
    EXIT("exit", "  * exit program"),
    ADDI("addi", "<param1>  <param2>   * simple integer add ",int.class, int.class ),
    ADDF("addf", "<param1>  <param2>   * simple float add ",float.class, float.class ),
    ECHO("echo", "<param1>  <param2>   * echos param1 string param2 times ",String.class, int.class );
    private String commandName, helpText;
    private Class[] params;

    MyFavoriteCommandType(String name, String helpText){
        commandName = name;
        this.helpText = helpText;
    }

    MyFavoriteCommandType(String name, String helpText, Class... params){
        commandName = name;
        this.helpText = helpText;
        this.params = params;
    }

    public String getName() {
        return commandName;
    }

    public String getHelpText() {
        return helpText;
    }

    public Class<?>[] getParamTypes(){
        return params;
    }
}
