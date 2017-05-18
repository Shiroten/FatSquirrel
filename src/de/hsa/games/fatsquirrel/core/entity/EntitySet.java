package de.hsa.games.fatsquirrel.core.entity;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.entity.character.Character;
import de.hsa.games.fatsquirrel.core.entity.character.HandOperatedMasterSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrel;

public class EntitySet {

    private final int numberOfMaxEntities;
    private Entity[] entityList;
    //private ArrayList<Entity> entityList = new ArrayList<Entity>();

    public EntitySet (XY size){
        numberOfMaxEntities = size.getX()*size.getY();
        this.entityList = new Entity[numberOfMaxEntities];

    }

    public int getNumberOfMaxEntities() {
        return numberOfMaxEntities;
    }

    public Entity[] getEntityList() {
        return entityList;
    }

    public Entity getEntity(int index) {return entityList[index]; }

    public void add(Entity toAdd) {
        for (int i = 0; i < numberOfMaxEntities; i++) {
            if (entityList[i] == null) {
                entityList[i] = toAdd;
                return;
            }
        }
    }

    public void delete(Entity toDelete) {

        for (int i = 0; i < numberOfMaxEntities; i++) {
            if (entityList[i] == toDelete) {
                entityList[i] = null;
                return;
            }
        }
    }

    public void nextStep(EntityContext flat) {
        for (int i = 0; i < numberOfMaxEntities; i++) {
            if (entityList[i] != null) {
                if(entityList[i] instanceof HandOperatedMasterSquirrel)
                    ((HandOperatedMasterSquirrel) entityList[i]).nextStep(flat);
                else if(entityList[i] instanceof Character)
                    ((Character) entityList[i]).nextStep(flat);
            }
        }
    }

    public XY collision(Entity entityToCheck, XY destination) {

        for (int i = 0; i < numberOfMaxEntities; i++) {
            Entity toCheck = entityList[i];
            if (toCheck != null) {
                if (toCheck.getCoordinate().getX() == destination.getX()
                        && toCheck.getCoordinate().getY() == destination.getY()) {
                    if (toCheck instanceof GoodPlant) {
                        entityToCheck.updateEnergy(toCheck.getEnergy());
                        this.delete(toCheck);
                        return destination;
                    }
                }
            }
        }
        return destination;
    }

    public MasterSquirrel getHandOperatedMasterSquirrel(){
        for(Entity e : entityList){
            if(e instanceof HandOperatedMasterSquirrel)
                return (MasterSquirrel) e;
        }
        return null;
    }
}
