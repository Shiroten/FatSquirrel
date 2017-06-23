package de.hsa.games.fatsquirrel.core.entity.squirrels;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.core.entity.Character;
import javafx.scene.paint.Color;

/**
 * The parent of all Squirrels. Implements stunning functionality
 */
public abstract class PlayerEntity extends Character {
    private int stunTime = 0;

    //Package Private
    PlayerEntity(int startEnergy, int id, XY coordinate, Color entityColor, Color entityTextColor, String name) {
        super(startEnergy, id, coordinate, entityColor, entityTextColor, name);
    }

    public void setStunTime(int stunTime) {
        this.stunTime = stunTime;
    }

    public int getStunTime() {
        return this.stunTime;
    }

    /**
     * Reduce stunTime by one
     */
    void reduceStunTime() {
        if (stunTime > 0)
            stunTime--;
    }

    //TODO: Im Namensrefactor
    String getName(BotControllerFactory factory) {
        return factory.getClass().getName().substring(34, factory.getClass().getName().length() - 7);
    }
}
