package de.hsa.games.fatsquirrel;

/**
 * Created by tillm on 04.04.2017.
 */
public enum ActionCommand {
    NORTH("NORTH", XY.UP),
    NORTHEAST("NORTHEAST", XY.RIGHT_UP),
    NORTHWEST("NORTHWEST", XY.LEFT_UP),
    EAST("EAST", XY.RIGHT),
    SOUTH("SOUTH", XY.DOWN),
    SOUTHEAST("SOUTHEAST", XY.RIGHT_DOWN),
    SOUTHWEST("SOUTHWEST", XY.LEFT_DOWN),
    WEST("WEST", XY.LEFT),
    NOWHERE("NOWHERE", XY.ZERO_ZERO),
    SPAWN;

    private String commandName;
    private XY direction;

    /**
     * @return the CommandName of the Object
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @return the Direction of the Object
     */
    public XY getDirection() {
        return direction;
    }

    /**
     * Constructor for a ActionCommand with commandName and direction
     *
     * @param commandName
     * @param direction
     */
    ActionCommand(String commandName, XY direction) {
        this.commandName = commandName;
        this.direction = direction;
    }

    ActionCommand() {
    }
}
