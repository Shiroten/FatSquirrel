package de.hsa.games.fatsquirrel;

public class XY {
    private final int x;
    private final int y;

    public static final XY ZERO_ZERO = new XY(0, 0);
    public static final XY RIGHT = new XY(1, 0);
    public static final XY LEFT = new XY(-1, 0);
    public static final XY UP = new XY(0, -1);
    public static final XY DOWN = new XY(0, 1);
    public static final XY RIGHT_UP = new XY(1, -1);
    public static final XY RIGHT_DOWN = new XY(1, 1);
    public static final XY LEFT_UP = new XY(-1, -1);
    public static final XY LEFT_DOWN = new XY(-1, 1);

    /**
     *
     * @return the X Value
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return the Y Value
     */
    public int getY() {
        return y;
    }

    /**
     * create new XY with X and Y
     * @param x
     * @param y
     */
    public XY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param xy adds XY Values to own Position
     * @return the new added Values of the own XY and the new xy
     */
    public XY plus(XY xy) {
        return new XY(x + xy.getX(), y + xy.getY());
    }

    /**
     *
     * @param xy subtract XY Values from the own Position
     * @return the new subtracted XY Values of own XY and the new XY
     */
    public XY minus(XY xy) {
        return new XY(x - xy.getX(), y - xy.getY());
    }

    /**
     *
     * @param factor multiplier for the function
     * @return the new XY with multiplied Values of X and Y with the factor
     */
    public XY times(int factor) {
        return new XY(x * factor, y * factor);
    }

    /**
     *
     * @return the length of the XY Object. Used to get the Length from Origin
     */
    public double length() {
        return Math.sqrt(Math.pow(Math.abs(x), 2) + Math.pow(Math.abs(y), 2));
    }

    /**
     *
     * @param xy Position of Comparision
     * @return the length of the XY Object to the other one
     */
    public double distanceFrom(XY xy) {
        return Math.sqrt(Math.pow(xy.getX() - x, 2) + Math.pow(xy.getY() - y, 2));
    }

    /**
     *
     * @param xy comparing Values
     * @return if the two XY have the same x and the same y Value
     */
    public boolean equals(XY xy) {
        try {
            if (xy.getX() == x && xy.getY() == y)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     *
     * @return the string representation of the XY obejct
     */
    public String toString() {
        return "x: " + x + " y: " + y;
    }

}
