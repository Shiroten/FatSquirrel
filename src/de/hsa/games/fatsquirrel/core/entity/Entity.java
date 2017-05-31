package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;
import javafx.scene.paint.Color;

public abstract class Entity {
    private final int id;
    protected int energy;
    private XY coordinate;

    private String entityName;
    private Color entityColor;

    private Color entityTextColor;

    public Entity(int id, XY coordinate) {
        this.id = id;
        this.coordinate = coordinate;
    }

    public Entity(int energy, int id, XY coordinate) {
        this.energy = energy;
        this.id = id;
        this.coordinate = coordinate;
    }

    public int getId() {
        return id;
    }

    public int getEnergy() {
        return energy;
    }

    public void updateEnergy(int energyDifference) {
        this.energy += energyDifference;
    }

    public XY getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(XY coordinate) {
        this.coordinate = coordinate;
    }

    public EntityType getEntityType() {
        return EntityType.NONE;
    }

    @Override
    public String toString() {
        return (getEntityType().toString() + " [ID: " + id + ", Energy: " + energy
                + ", Coordinate: (X: " + coordinate.getX() + ", Y: " + coordinate.getY() + ")]");
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Entity) {
            Entity e = (Entity) o;
            boolean returnValue;
            returnValue = this.id == e.getId();
            return returnValue;
        }
        return false;

    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Color getEntityColor() {
        return entityColor;
    }

    public void setEntityColor(Color entityColor) {
        this.entityColor = entityColor;
    }

    public Color getEntityTextColor() {
        return entityTextColor;
    }

    public void setEntityTextColor(Color entityTextColor) {
        this.entityTextColor = entityTextColor;
    }
}
