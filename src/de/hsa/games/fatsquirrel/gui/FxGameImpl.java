package de.hsa.games.fatsquirrel.gui;

import de.hsa.games.fatsquirrel.ActionCommand;
import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.core.State;
import de.hsa.games.fatsquirrel.core.entity.Entity;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.squirrels.MiniSquirrel;
import de.hsa.games.fatsquirrel.util.ui.Command;

/**
 * The Game implementation that utilizes the FxUI
 */
public class FxGameImpl extends Game {


    private Command imploadMiniSquirrel = null;

    /**
     * Constructor for FXGameImpl
     *
     * @param fxUI
     * @param state
     */
    public FxGameImpl(FxUI fxUI, State state) {
        this.setUi(fxUI);
        this.setState(state);
        this.handOperatedMasterSquirrel = this.getState().getBoard().getHandOperatedMasterSquirrel();
    }

    /**
     * Processed the Input from the UI for HandOperatedMasterSquirrel
     */
    protected void processInput() {

        if (handOperatedMasterSquirrel != null) {
            Command cmd = this.getUi().getCommand();
            switch (cmd.getCommandTypeInfo().getName()) {
                case "w":
                    handOperatedMasterSquirrel.setCommand(ActionCommand.NORTH);
                    break;
                case "a":
                    handOperatedMasterSquirrel.setCommand(ActionCommand.WEST);
                    break;
                case "d":
                    handOperatedMasterSquirrel.setCommand(ActionCommand.EAST);
                    break;
                case "s":
                    handOperatedMasterSquirrel.setCommand(ActionCommand.SOUTH);
                    break;
                case "f":
                    handOperatedMasterSquirrel.setCommand(ActionCommand.SPAWN);
                    handOperatedMasterSquirrel.setMiniSquirrelSpawnEnergy((Integer) (cmd.getParams())[0]);
                    break;
                case "t":
                    imploadMiniSquirrel = cmd;
                    break;
                case "#":
                    addGameSpeed(1);
                    break;
                case "+":
                    addGameSpeed(-1);
                    break;
                default:
                    handOperatedMasterSquirrel.setCommand(ActionCommand.NOWHERE);
            }
        } else {
            System.out.println("No HandOperatedMasterSquirrel found");
        }
    }

    /**
     * Render the next Frame
     */
    protected void render() {
        this.getUi().render(this.getState().flattenBoard());
    }

    /**
     * Execute the update() of the state and sets the message String for the UI
     */
    protected void update() {

        if (imploadMiniSquirrel != null) {
            int implosionRadius = (Integer) (imploadMiniSquirrel.getParams())[0];
            imploadMiniSquirrel(implosionRadius);
            imploadMiniSquirrel = null;
        }

        getState().update();
        FxUI fxUI = (FxUI) this.getUi();
        StringBuilder msg = new StringBuilder();
        msg.append(String.format("FrameRate: %2.0f", 1000.0 / this.getTickLength()));
        msg.append(String.format(" | Remaining GameTime: %d", this.getState().getBoard().getRemainingGameTime()));
        msg.append(String.format(" | MasterSquirrel Energy: "));
        msg.append(handOperatedMasterSquirrel.getEnergy());

        fxUI.message(msg.toString());
    }

    /**
     * Gives the implosion order for HandOperatedMasterSquirrel MiniSquirrels     *
     *
     * @param implosionRadius
     */
    private void imploadMiniSquirrel(int implosionRadius) {
        for (Entity e : getState().getEntitySet()) {
            if (e.getEntityType() == EntityType.MINISQUIRREL) {
                if (((MiniSquirrel) e).getDaddy() == handOperatedMasterSquirrel) {
                    ((MiniSquirrel) e).implode(implosionRadius);
                }

            }
        }
    }

}
