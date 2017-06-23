package de.hsa.games.fatsquirrel.core.entity;

public enum EntityType {
    BADBEAST(BadBeast.class.getName()),
    GOODBEAST(GoodBeast.class.getName()),
    BADPLANT(BadPlant.class.getName()),
    GOODPLANT(GoodPlant.class.getName()),
    WALL(Wall.class.getName()),
    MASTERSQUIRREL, MINISQUIRREL, NONE;

    private String className;
    private EntityType(){
    }

    private EntityType(String s){
        this.className = s;
    }

    public String getClassName(){
        return this.className;
    }
}
