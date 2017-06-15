package de.hsa.games.fatsquirrel;

/**
 * All commands the player can give his MasterSquirrel.
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
     * @param commandName The name of the command
     * @param direction The associated direction of the command
     */
    ActionCommand(String commandName, XY direction) {
        this.commandName = commandName;
        this.direction = direction;
    }

    ActionCommand() {
    }
}
