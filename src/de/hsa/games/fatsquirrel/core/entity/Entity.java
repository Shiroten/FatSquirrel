package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;
import javafx.scene.paint.Color;

/**
 * Everything that is on the board
 */
public abstract class Entity {
    private final int id;
    protected int energy;
    private XY coordinate;

    private String entityName;
    private Color entityColor;
    private Color entityTextColor;

    public Entity(int id, XY coordinate,Color entityColor, Color entityTextColor, String name) {
        this.id = id;
        this.coordinate = coordinate;
        this.entityColor = entityColor;
        this.entityTextColor = entityTextColor;
        this.entityName = name;
    }

    public Entity(int energy, int id, XY coordinate, Color entityColor, Color entityTextColor, String name) {
        this.energy = energy;
        this.id = id;
        this.coordinate = coordinate;
        this.entityColor = entityColor;
        this.entityTextColor = entityTextColor;
        this.entityName = name;
    }

    public int getId() {
        return id;
    }

    public int getEnergy() {
        return energy;
    }

    public void updateEnergy(int energyDifference){
        this.energy += energyDifference;
        if(energy < 0)
            energy = 0;
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

    //TODO: Ändern
    @Override
    public String toString() {
        return (getEntityType().toString() + " [ID: " + id + ", Energy: " + energy
                + ", Coordinate: (X: " + coordinate.getX() + ", Y: " + coordinate.getY() + ")]");
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Entity) {
            Entity e = (Entity) o;
            return this.id == e.getId();
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
