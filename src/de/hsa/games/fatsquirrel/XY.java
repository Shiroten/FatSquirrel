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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public XY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public XY(XY end, XY start) {
        this.x = end.getX() - start.getX();
        this.y = end.getY() - start.getY();
    }

    public XY plus(XY xy) {
        return new XY(x + xy.getX(), y + xy.getY());
    }

    public XY minus(XY xy) {
        return new XY(x - xy.getX(), y - xy.getY());
    }

    public XY times(int factor) {
        return new XY(x * factor, y * factor);
    }

    public double length() {
        return Math.sqrt(Math.pow(Math.abs(x), 2) + Math.pow(Math.abs(y), 2));
    }

    public double distanceFrom(XY xy) {
        return Math.sqrt(Math.pow(xy.getX() - x, 2) + Math.pow(xy.getY() - y, 2));
    }

    public boolean equals(XY xy) {
        try {
            if (xy.getX() == x && xy.getY() == y)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public String toString() {
        return "x: " + x + " y: " + y;
    }

}
