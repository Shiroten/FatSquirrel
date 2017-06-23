package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.BadBeast;
import org.junit.Test;

import static org.junit.Assert.*;

public class BadBeastTest {

    @Test
    public void entityTypeTest(){
        BadBeast badBeast = new BadBeast(2, new XY(1,1));
        System.out.println(badBeast.toString());
        assertEquals(EntityType.BADBEAST, badBeast.getEntityType());
    }

}