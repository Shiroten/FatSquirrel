package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.squirrels.HandOperatedMasterSquirrel;
import org.junit.Test;

import static org.junit.Assert.*;

public class MasterSquirrelBotTest {

    @SuppressWarnings("EmptyMethod")
    @Test
    public void nextStep() throws Exception {

    }

    @Test
    public void getEntityTest(){
        HandOperatedMasterSquirrel handOperatedMasterSquirrel = new HandOperatedMasterSquirrel(1, new XY(1,1));
        System.out.println(handOperatedMasterSquirrel.getEntityName());
        assertEquals(EntityType.MASTERSQUIRREL, handOperatedMasterSquirrel.getEntityType());
    }

}