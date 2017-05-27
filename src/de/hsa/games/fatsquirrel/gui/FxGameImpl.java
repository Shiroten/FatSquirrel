package de.hsa.games.fatsquirrel.gui;

import de.hsa.games.fatsquirrel.ActionCommand;
import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.core.State;
import de.hsa.games.fatsquirrel.core.entity.Entity;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.character.HandOperatedMasterSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MiniSquirrel;
import de.hsa.games.fatsquirrel.util.ui.Command;

public class FxGameImpl extends Game {

    private HandOperatedMasterSquirrel handOperatedMasterSquirrel;
    private Command imploadMiniSquirrel = null;

    protected FxGameImpl() {
    }

    public FxGameImpl(FxUI fxUI, State state) {

        this.setUi(fxUI);
        this.setState(state);
        this.handOperatedMasterSquirrel = this.getState().getBoard().getHandOperatedMasterSquirrel();
    }

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
                case "p":
                    handOperatedMasterSquirrel.updateEnergy(1000);
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

    protected void render() {
        this.getUi().render(this.getState().flattenBoard());
    }

    protected void update() {

        if (imploadMiniSquirrel != null) {
            int implosionRadius = (Integer)(imploadMiniSquirrel.getParams())[0];
            imploadMiniSquirrel(implosionRadius);
            imploadMiniSquirrel = null;
        }

        getState().update();
        FxUI fxUI = (FxUI) this.getUi();
        String msg = "";
        msg = msg + String.format("FrameRate: %2.0f", 1000.0/this.getTickLength());
        msg = msg + String.format(" | Remaining GameTime: %d", this.getState().getBoard().getRemainingGameTime());
        msg = msg + String.format(" | MasterSquirrel Energy: " + Integer.toString(handOperatedMasterSquirrel.getEnergy()));

        fxUI.message(msg);
    }


    private void imploadMiniSquirrel(int implosionRadius) {
        for (Entity e : getState().getEntitySet()) {
            if (e != null) {
                if (e.getEntityType() == EntityType.MINISQUIRREL) {
                    if (((MiniSquirrel) e).getDaddy() == handOperatedMasterSquirrel) {
                        ((MiniSquirrel) e).implode(implosionRadius);
                    }

                }
            }
        }
    }
}
