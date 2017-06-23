package de.hsa.games.fatsquirrel.core.entity;


public enum EntityType {
    BADBEAST(BadBeast.class.getSimpleName(), "BB"),
    GOODBEAST(GoodBeast.class.getSimpleName(), "GB"),
    BADPLANT(BadPlant.class.getSimpleName(), "BP"),
    GOODPLANT(GoodPlant.class.getSimpleName(), "GP"),
    WALL(Wall.class.getSimpleName(), "WA"),
    MASTERSQUIRREL("MS"),
    MINISQUIRREL("mS"),
    NONE;

    private String className;
    private String typeToString;

    EntityType() {
    }

    EntityType(String typeToString) {
        this.typeToString = typeToString;
    }

    EntityType(String className, String typeToString) {
        this.className = className;
        this.typeToString = typeToString;
    }
    public String getTypeToString(){
        return this.typeToString;
    }

    public String getClassName() {
        return this.className;
    }
}
