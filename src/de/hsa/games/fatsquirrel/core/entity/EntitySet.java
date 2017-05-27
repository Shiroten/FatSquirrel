package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.entity.character.Character;
import de.hsa.games.fatsquirrel.core.entity.character.HandOperatedMasterSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrel;

import java.util.*;

public class EntitySet {

    private List<Entity> entityList = new ArrayList<>();

    public List<Entity> getEntityList() {
        return entityList;
    }

    public Entity getEntity(int index) {return entityList.get(index); }

    public void add(Entity toAdd) {
        entityList.listIterator().add(toAdd);
    }

    public void delete(Entity toDelete) {
        entityList.remove(toDelete);
    }

    public boolean contains(Entity e){
        return entityList.contains(e);
    }

    public void nextStep(EntityContext flat) {

        try {
            for(Entity e : new ArrayList<>(entityList)){
                if(entityList.contains(e)) {
                    if (e instanceof HandOperatedMasterSquirrel)
                        ((HandOperatedMasterSquirrel) e).nextStep(flat);
                    else if (e instanceof Character)
                        ((Character) e).nextStep(flat);
                }
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
