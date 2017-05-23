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


    @org.junit.Before
    public void testSetUp() throws Exception {

    }

    @org.junit.Test
    public void plus() throws Exception {
    XY expectedValue = new XY (30,30);
    XY addedValue = test1.plus(test2);

    assertTrue(expectedValue.equals(addedValue));
    }

    @org.junit.Test
    public void minus() throws Exception {
        XY expectedValue = new XY (10,10);
        XY subtractedValue = test2.minus(test1);

        assertTrue(expectedValue.equals(subtractedValue));
    }

    @org.junit.Test
    public void times() throws Exception {
        XY expectedValue = new XY (70,70);
        XY multipliedValue = test1.times(7);
        assertTrue(expectedValue.equals(multipliedValue));
    }

    @org.junit.Test
    public void length() throws Exception {
        double vectorLength1 = testLength1.length();
        double expectedValue1 = 15;

        boolean firstTest = vectorLength1 == expectedValue1;

        double vectorLength2 = testLength2.length();
        double expectedValue2 = Math.sqrt(458);

        boolean secondTest = vectorLength2 == expectedValue2;

        assertTrue(firstTest && secondTest);
    }

    @org.junit.Test
    public void distanceFrom() throws Exception {

        assertEquals(15.0, testDistanceFrom1.distanceFrom(testDistanceFrom3), 0.001);
        assertEquals(Math.sqrt(965), testDistanceFrom2.distanceFrom(testDistanceFrom3), 0.001);
        assertEquals(0.0, testDistanceFrom3.distanceFrom(testDistanceFrom3), 0.001);
    }

    @org.junit.Test
    public void equals() throws Exception {
        boolean isEquals = test1.equals(new XY (10,10));
        boolean isNotEquals = test1.equals(new XY (11,11));

        assertTrue(isEquals && !isNotEquals);
    }

    @org.junit.Test
    public void testToString() throws Exception {
    }

}