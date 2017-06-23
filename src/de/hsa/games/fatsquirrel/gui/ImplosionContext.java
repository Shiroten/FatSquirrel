package de.hsa.games.fatsquirrel.gui;

import de.hsa.games.fatsquirrel.XY;

/**
 * Manages information on a implosion of a minisquirrel
 */
public class ImplosionContext {

    private final int energyLoss;
    private final int radius;
    private final XY position;
    private int tickCounter;
    private final int MAX_TICK_COUNTER;

    public ImplosionContext(int energyLoss, int radius, XY position) {
        this.energyLoss = energyLoss;
        this.radius = radius;
        this.position = position;
        MAX_TICK_COUNTER = calcTickCounter(energyLoss);
        this.tickCounter = MAX_TICK_COUNTER;
    }

    private int calcTickCounter(int energyLoss) {

        if (energyLoss > 1000) {
            return 600;
        } else if (energyLoss > 500) {
            return 360;
        } else if (energyLoss > 100) {
            return 240;
        } else if (energyLoss > 10) {
            return 180;
        } else {
            return 0;
        }

    }

    int getEnergyLoss() {
        return energyLoss;
    }

    int getRadius() {
        return radius;
    }

    XY getPosition() {
        return position;
    }

    int getTickCounter() {
        return tickCounter;
    }

    int getMAX_TICK_COUNTER() {
        return MAX_TICK_COUNTER;
    }

    void updateTick() {
        tickCounter = tickCounter - 2;
    }


}
