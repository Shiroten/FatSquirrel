package de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.core.FullFieldException;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.character.Character;

import java.util.*;


/**
 * Created by tillm on 02.06.2017.
 */
public class PathFinder {
    private List<Node> openList;
    private List<Node> closedList;

    private static class Node {
        private final XY coordinate;
        private double fx;
        private Node predecessor;

        Node(XY coordinate) {
            this.coordinate = coordinate;
        }

        public XY getCoordinate() {
            return coordinate;
        }

        double getFx() {
            return fx;
        }

        void setFx(double fx) {
            this.fx = fx;
        }

        Node getPredecessor() {
            return predecessor;
        }

        void setPredecessor(Node predecessor) {
            this.predecessor = predecessor;
        }
    }

    //TODO: Adapter implementieren
    public XY directionTo(XY from, XY destination, ControllerContext context) throws FullFieldException {
        openList = new ArrayList<>();
        closedList = new ArrayList<>();

        openList.add(new Node(from));

        if (!isWalkable(destination, context))
            throw new FullFieldException();

        while (!openList.isEmpty()) {
            Node currentNode = popMinF(openList);
            if (currentNode.getCoordinate().equals(destination))
                return getSecondNode(currentNode).coordinate.minus(from);

            closedList.add(currentNode);
            expandNode(currentNode, context, destination);
        }
        return XY.ZERO_ZERO;
    }

    private void expandNode(Node currentNode, ControllerContext context, XY destination) {
        for (XY xy : XYsupport.directions()) {
            Node successor = new Node(currentNode.getCoordinate().plus(xy));
            if (containsPosition(closedList, successor.coordinate) != 0 || !isWalkable(successor.getCoordinate(), context))
                continue;

            double tentativeFx = XYsupport.distanceInSteps(successor.getCoordinate(), destination);
            successor.setFx(tentativeFx);

            int position = containsPosition(openList, successor.coordinate);
            if (position != 0) {
                if (openList.get(position).getFx() > tentativeFx) {
                    openList.remove(position);
                    openList.add(successor);
                }
            } else
                openList.add(successor);

            successor.setPredecessor(currentNode);
        }
    }

    //TODO: EnemySquirrel mit einebeziehen
    private boolean isWalkable(XY coordinate, ControllerContext context) {
        EntityType entityTypeAtNewField = null;
        try {
            entityTypeAtNewField = context.getEntityAt(coordinate);
        } catch (OutOfViewException e) {
            //Todo: add to log
            // e.printStackTrace();
        }
        return entityTypeAtNewField != EntityType.WALL
                && entityTypeAtNewField != EntityType.BADBEAST
                && entityTypeAtNewField != EntityType.BADPLANT;
    }

    private Node popMinF(List<Node> openList) {
        Node min = null;
        for (Node n : openList) {
            if (min == null)
                min = n;
            else if (n.getFx() < min.getFx())
                min = n;
        }
        openList.remove(min);

        return min;
    }

    private int containsPosition(List<Node> openList, XY position) {
        for (Node n : openList) {
            if (n.coordinate.equals(position))
                return openList.indexOf(n);
        }

        return 0;
    }

    private Node getSecondNode(Node lastNode) {
        Node predecessor = lastNode;
        if (predecessor.getPredecessor() == null)
            return predecessor;
        while (predecessor.getPredecessor().getPredecessor() != null) {
            predecessor = predecessor.getPredecessor();
        }

        return predecessor;
    }

    public XY goodMove(EntityContext view, XY directionVector, Character character) {
        XYsupport.Rotation rotation = XYsupport.Rotation.clockwise;
        int nor = 1;
        XY checkPosition = character.getCoordinate().plus(directionVector);
        if (freeField(view, checkPosition, character)) {
            return directionVector;
        }
        XY newVector;
        while (true) {
            newVector = XYsupport.rotate(rotation, directionVector, nor);
            checkPosition = character.getCoordinate().plus(newVector);
            if (freeField(view, checkPosition, character)) {
                return newVector;
            } else {
                if (rotation == XYsupport.Rotation.clockwise) {
                    rotation = XYsupport.Rotation.anticlockwise;
                } else {
                    rotation = XYsupport.Rotation.clockwise;
                    nor++;
                }
                if (nor > 3)
                    return XYsupport.oppositeVector(directionVector);
            }
        }
    }

    private boolean freeField(EntityContext view, XY location, Character character) {
        EntityType et = view.getEntityType(location);
        switch (character.getEntityType()) {
            case MASTERSQUIRREL:
                switch (et) {
                    case WALL:
                    case BADBEAST:
                    case BADPLANT:
                    case MASTERSQUIRREL:
                        return false;
                    case NONE:
                    case GOODBEAST:
                    case GOODPLANT:
                    case MINISQUIRREL:
                        return true;
                }
                break;
            case MINISQUIRREL:
                switch (et) {
                    case NONE:
                        return true;
                    default:
                        return false;
                }
            case GOODBEAST:
                switch (et) {
                    case NONE:
                        return true;
                    default:
                        return false;
                }

            case BADBEAST:
                switch (et) {
                    case NONE:
                    case MASTERSQUIRREL:
                    case MINISQUIRREL:
                        return true;
                    default:
                        return false;
                }
        }
        return false;
    }

}

