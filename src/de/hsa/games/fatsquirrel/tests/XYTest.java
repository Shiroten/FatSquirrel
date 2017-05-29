package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;

import static org.junit.Assert.*;

public class XYTest {
    private XY test1 = new XY(10,10);
    private XY test2 = new XY(20,20);
    private XY testLength1 = new XY(9,12);
    private XY testLength2 = new XY (13,17);
    private XY testDistanceFrom1 = new XY(15,15);
    private XY testDistanceFrom2 = new XY(23,29);
    private XY testDistanceFrom3 = new XY(6,3);

    @org.junit.Test
    public void plus() throws Exception {
        assertEquals(new XY(30, 30), test1.plus(test2));
    }

    @org.junit.Test
    public void minus() throws Exception {
        assertEquals(new XY(10, 10), test2.minus(test1));
    }

    @org.junit.Test
    public void times() throws Exception {
        assertEquals(new XY(70,70), test1.times(7));
    }

    @org.junit.Test
    public void length() throws Exception {

        assertEquals(15, testLength1.length(), 0.01);
        assertEquals(Math.sqrt(458), testLength2.length(), 0.001);
    }

    @org.junit.Test
    public void distanceFrom() throws Exception {

        assertEquals(15.0, testDistanceFrom1.distanceFrom(testDistanceFrom3), 0.001);
        assertEquals(Math.sqrt(965), testDistanceFrom2.distanceFrom(testDistanceFrom3), 0.001);
        assertEquals(0.0, testDistanceFrom3.distanceFrom(testDistanceFrom3), 0.001);
    }

    @org.junit.Test
    public void equals() throws Exception {
        assertTrue(test1.equals(new XY(10, 10)));
        assertFalse(test1.equals(new XY(11, 10)));


    }

    @org.junit.Test
    public void testToString() throws Exception {
    }

}