package de.hsa.games.fatsquirrel.util.ui;

/**
 * A sophisticated definition of console commands for the console version of the game.
 */
public enum GameCommandType implements CommandTypeInfo {
    HELP("h", "help", "* list all commands"),
    EXIT("e", "exit", "* exit the game"),
    ALL("all", "all", "* i have no idea what this does"),
    LEFT("a", "moveLeft" ,"* move the squirrel left if possible"),
    RIGHT("d", "moveRight" ,"* move the squirrel right if possible"),
    UP("w", "moveUp" ,"* move the squirrel up if possible"),
    DOWN("s","moveDown" ,"* move the squirrel down if possible"),
    MASTER_ENERGY("i", "masterEnergy" ,"* the energy of the mastersquirrel"),
    SPAWN_MINI("f", "spawnMini" ,"<param1> Integer  *spawn a mini-squirrel with param1 Energy", int.class),
    CHEAT_ENERGY("p", "addEnergy" ,"Adds 1000 Energy to MasterSquirrel"),
    IMPLODE_MINISQUIRRELS("t", "implode" ,"Implode all MiniSquirrel of Player"),
    ADD_GAME_SPEED("+","gameSpeed+","Inkrement the Game Speed"),
    SUBTRACT_GAME_SPEED("#","gameSpeed-","Dekrement the Game Speed"),
    NOTHING("", "doNothing", "* Just press Enter");

    private final String commandName;
    private final String helpText;
    private final String methodName;
    private Class[] params;

    GameCommandType(String name, String methodName, String helpText){
        commandName = name;
        this.helpText = helpText;
        this.methodName = methodName;
    }

    GameCommandType(String name, String methodName, String helpText, Class... params){
        commandName = name;
        this.helpText = helpText;
        this.methodName = methodName;
        this.params = params;
    }

    public String getName() {
        return commandName;
    }

    public String getHelpText() {
        return helpText;
    }

    public String getMethodName(){ return methodName; }

    public Class<?>[] getParamTypes(){
        return params;
    }
}
