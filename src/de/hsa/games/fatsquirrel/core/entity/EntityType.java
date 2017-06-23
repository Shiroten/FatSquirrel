package de.hsa.games.fatsquirrel.core.entity;

import java.util.ArrayList;
import java.util.List;

public enum EntityType {
    BADBEAST(BadBeast.class.getSimpleName()),
    GOODBEAST(GoodBeast.class.getSimpleName()),
    BADPLANT(BadPlant.class.getSimpleName()),
    GOODPLANT(GoodPlant.class.getSimpleName()),
    WALL(Wall.class.getSimpleName()),
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
