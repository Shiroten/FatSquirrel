package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.entity.character.Character;
import de.hsa.games.fatsquirrel.core.entity.character.HandOperatedMasterSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrel;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class EntitySet {

    private final int numberOfMaxEntities;
    private ArrayList<Entity> entityList = new ArrayList<Entity>();

    public EntitySet (XY xy){
        numberOfMaxEntities = xy.getX() * xy.getY();
    }

    public int getNumberOfMaxEntities() {
        return numberOfMaxEntities;
    }

    public ArrayList<Entity> getEntityList() {
        return entityList;
    }

    public Entity getEntity(int index) {return entityList.get(index); }

    public void add(Entity toAdd) {
        entityList.add(toAdd);
    }

    public void delete(Entity toDelete) {
        entityList.remove(toDelete);
    }

    public void nextStep(EntityContext flat) {
        try {
            for (Entity e : entityList) {
                if (e instanceof HandOperatedMasterSquirrel)
                    ((HandOperatedMasterSquirrel) e).nextStep(flat);
                else if (e instanceof Character)
                    ((Character) e).nextStep(flat);
            }
        } catch (ConcurrentModificationException e){
            e.printStackTrace();
        }
    }

    public MasterSquirrel getHandOperatedMasterSquirrel(){
        for(Entity e : entityList){
            if(e instanceof HandOperatedMasterSquirrel)
                return (MasterSquirrel) e;
        }
        return null;
    }

    public int getNumberOfEntities(){
        return entityList.size();
    }
}
