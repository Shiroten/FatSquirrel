package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.character.HandOperatedMasterSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by tillm on 19.05.2017.
 */
public class MasterSquirrelBotTest {

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