package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;

import static org.junit.Assert.assertTrue;

public class XYTest {
    private XY test1 = new XY(10,10);
    private XY test2 = new XY(20,20);
    private XY testLength1 = new XY(9,12);
    private XY testLength2 = new XY (13,17);
    private XY testDistanceFrome1 = new XY(15,15);
    private XY testDistanceFrome2 = new XY(23,29);
    private XY testDistanceFrome3 = new XY(6,3);


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
        double vectorLength1 = testDistanceFrome1.distanceFrom(testDistanceFrome3);
        double expectedValue1 = 15;

        boolean firstTest = vectorLength1 == expectedValue1;

        double vectorLength2 = testDistanceFrome2.distanceFrom(testDistanceFrome3);
        double expectedValue2 = Math.sqrt(965);

        boolean secondTest = vectorLength2 == expectedValue2;

        double vectorLength3 = testDistanceFrome3.distanceFrom(testDistanceFrome3);
        double expectedValue3 = 0.0;

        boolean thirdTest = vectorLength3 == expectedValue3;

        assertTrue(firstTest && secondTest && thirdTest);
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