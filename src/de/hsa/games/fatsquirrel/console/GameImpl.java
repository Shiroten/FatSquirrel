package de.hsa.games.fatsquirrel.console;

import de.hsa.games.fatsquirrel.ActionCommand;
import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.core.State;
import de.hsa.games.fatsquirrel.core.entity.squirrels.HandOperatedMasterSquirrel;
import de.hsa.games.fatsquirrel.util.ui.Command;
import de.hsa.games.fatsquirrel.util.ui.CommandTypeInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The console implementation of the game
 */
public class GameImpl extends Game {

    private HandOperatedMasterSquirrel handOperatedMasterSquirrel;


    public GameImpl() {
        this.setUi(new ConsoleUI());
        this.setState(new State());
        this.handOperatedMasterSquirrel = this.getState().getBoard().getHandOperatedMasterSquirrel();
    }

    protected void processInput() {

        Command command = this.getUi().getCommand();

        try {
            Method method = this.getClass().getDeclaredMethod(((GameCommandType) command.getCommandTypeInfo()).getMethodName(), command.getCommandTypeInfo().getParamTypes());
            method.invoke(this, command.getParams());
        } catch (IllegalAccessException | NullPointerException | InvocationTargetException | NoSuchMethodException iae) {
            iae.printStackTrace();
        }

    }

    private void exit() {
        System.exit(0);
    }

    private void help() {
        for (CommandTypeInfo i : GameCommandType.values()) {
            System.out.println(i.getName() + " " + i.getHelpText());
        }
    }

    private void moveUp() {
        handOperatedMasterSquirrel.setCommand(ActionCommand.NORTH);
    }

    private void moveDown() {
        handOperatedMasterSquirrel.setCommand(ActionCommand.SOUTH);
    }

    private void moveLeft() {
        handOperatedMasterSquirrel.setCommand(ActionCommand.WEST);
    }

    private void moveRight() {
        handOperatedMasterSquirrel.setCommand(ActionCommand.EAST);
    }

    private void masterEnergy() {
        System.out.println("Energy vom MasterSquirrel: " + handOperatedMasterSquirrel.getEnergy());
    }


    private void doNothing(){

    }

    protected void render() {
        this.getUi().render(this.getState().flattenBoard());
    }

    protected void update() {
        getState().update();
    }
}
