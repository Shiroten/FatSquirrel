package de.hsa.games.fatsquirrel.core.entity;

import java.util.ArrayList;
import java.util.List;

public enum EntityType {
    BADBEAST(BadBeast.class.getName()),
    GOODBEAST(GoodBeast.class.getName()),
    BADPLANT(BadPlant.class.getName()),
    GOODPLANT(GoodPlant.class.getName()),
    WALL(Wall.class.getName()),
    MASTERSQUIRREL,
    MINISQUIRREL,
    NONE;

    private String className;
    EntityType(){
    }

    EntityType(String s){
        this.className = s;
    }

    public String getClassName(){
        return this.className;
    }
}
