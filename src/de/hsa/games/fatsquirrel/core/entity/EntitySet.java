package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.core.entity.character.Character;
import de.hsa.games.fatsquirrel.core.entity.character.HandOperatedMasterSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrel;

import java.util.*;

/**
 * Contains all entities on the field
 */
public class EntitySet {

    private List<Entity> entityList = new ArrayList<>();

    /**
     *
     * @return The list with all entities in the game
     */
    public List<Entity> getEntityList() {
        return entityList;
    }

    /**
     *
     * @param toAdd The entity to be added
     */
    public void add(Entity toAdd) {
        entityList.listIterator().add(toAdd);
    }

    /**
     *
     * @param toDelete The entity to be deleted
     */
    public void delete(Entity toDelete) {
        entityList.remove(toDelete);
    }

    /**
     *
     * @param entity The entity to be checked
     * @return true if the entity is in the list, false else
     */
    public boolean contains(Entity entity){
        return entityList.contains(entity);
    }

    /**
     * Calls the nextStep-Method of all Characters in the game
     * @param context The context, on which the characters perform their actions
     */
    public void nextStep(EntityContext context) {

        try {
            for(Entity e : new ArrayList<>(entityList)){
                if(entityList.contains(e)) {
                    if (e instanceof Character)
                        ((Character) e).nextStep(context);
                }
            }
        } catch (ConcurrentModificationException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @return The handOperatedMasterSquirrel in the game
     */
    public MasterSquirrel getHandOperatedMasterSquirrel(){
        for(Entity e : entityList){
            if(e instanceof HandOperatedMasterSquirrel)
                return (MasterSquirrel) e;
        }
        return null;
    }
}
