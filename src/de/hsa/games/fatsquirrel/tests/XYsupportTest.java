package de.hsa.games.fatsquirrel.tests;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import org.junit.Test;

import static org.junit.Assert.*;

public class XYsupportTest {
    private XY test1 = new XY(9, -12);
    private XY test2 = new XY(-13, 17);


    @Test
    public void oppositeVector() throws Exception {
        assertEquals(new XY(-9, 12), XYsupport.oppositeVector(test1));
        assertEquals(new XY(13, -17), XYsupport.oppositeVector(test2));
    }

    @Test
    public void rotate() throws Exception {
        XY rotatedOneTimeClockwise = XYsupport.rotate(XYsupport.Rotation.clockwise, XY.UP, 1);
        XY rotatedThreeTimeClockwise = XYsupport.rotate(XYsupport.Rotation.clockwise, XY.UP, 3);
        XY rotatedFiveTimeClockwise = XYsupport.rotate(XYsupport.Rotation.clockwise, XY.UP, 5);
        XY rotatedOneTimeAntiClockwise = XYsupport.rotate(XYsupport.Rotation.anticlockwise, XY.UP, 1);
        XY rotatedThreeTimeAntiClockwise = XYsupport.rotate(XYsupport.Rotation.anticlockwise, XY.UP, 3);
        XY rotatedFiveTimeAntiClockwise = XYsupport.rotate(XYsupport.Rotation.anticlockwise, XY.UP, 5);

        boolean firstTest = rotatedOneTimeClockwise.equals(XY.RIGHT_UP) && rotatedOneTimeAntiClockwise.equals(XY.LEFT_UP);
        boolean secondTest = rotatedThreeTimeClockwise.equals(XY.RIGHT_DOWN) && rotatedThreeTimeAntiClockwise.equals(XY.LEFT_DOWN);
        boolean thirdTest = rotatedFiveTimeClockwise.equals(XY.LEFT_DOWN) && rotatedFiveTimeAntiClockwise.equals((XY.RIGHT_DOWN));
        assertTrue(firstTest && secondTest && thirdTest);
    }

    @Test
    public void isInRange() throws Exception {
        XY lowerLeftCorner = new XY(0,10);
        XY upperRightCorner = new XY(10, 0);
        XY trueMiddle = new XY (2, 2);
        XY falseMiddle = new XY(3, 11);
        assertTrue("Punkt ist zwischen zwei Punkten", XYsupport.isInRange(trueMiddle, lowerLeftCorner, upperRightCorner));
        assertFalse("Punkt ist nicht zwischen zwei Punkten", XYsupport.isInRange(falseMiddle, lowerLeftCorner, upperRightCorner));
    }

    @Test
    public void normalizedVector() throws Exception {

        assertEquals(XY.RIGHT_DOWN, XYsupport.normalizedVector(new XY(10, 10)));
        assertEquals(XY.RIGHT_DOWN, XYsupport.normalizedVector(new XY(10, 11)));
        assertEquals(XY.RIGHT, XYsupport.normalizedVector(new XY(5, 1)));
        assertEquals(XY.RIGHT, XYsupport.normalizedVector(new XY(5,-2)));
        assertEquals(XY.RIGHT_UP, XYsupport.normalizedVector(new XY(5,-3)));
        assertEquals(XY.ZERO_ZERO, XYsupport.normalizedVector(new XY(0,0)));
        assertEquals(XY.LEFT_UP, XYsupport.normalizedVector(new XY(-10, -11)));
    }

}