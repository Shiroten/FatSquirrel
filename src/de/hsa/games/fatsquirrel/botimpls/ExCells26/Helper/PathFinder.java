package de.hsa.games.fatsquirrel.botimpls.ExCells26.Helper;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.core.FullFieldException;
import de.hsa.games.fatsquirrel.core.entity.BadPlant;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.GoodPlant;
import de.hsa.games.fatsquirrel.core.entity.character.GoodBeast;

import java.util.*;


/**
 * Created by tillm on 02.06.2017.
 */
public class PathFinder {
    private List<Node> openList;
    private List<Node> closedList;
    private ControllerContext context;
    private BotCom botCom;

    public PathFinder(BotCom botCom){
        this.botCom = botCom;
    }


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

    public XY directionTo(XY from, XY destination, ControllerContext context) throws FullFieldException, FieldUnreachableException {
        openList = new ArrayList<>();
        closedList = new ArrayList<>();

        openList.add(new Node(from));
        this.context = context;
        this.botCom = botCom;

        if (!isWalkable(destination))
            throw new FullFieldException();

        while (!openList.isEmpty()) {
            Node currentNode = popMinF(openList);
            if (currentNode.getCoordinate().equals(destination)) {
                //System.out.println(closedList.size());
                return getSecondNode(currentNode).coordinate.minus(from);
            }

            closedList.add(currentNode);
            expandNode(currentNode, destination);
        }
        throw new FieldUnreachableException();
    }

    private void expandNode(Node currentNode, XY destination) {
        for (XY xy : XYsupport.directions()) {
            Node successor = new Node(currentNode.getCoordinate().plus(xy));
            if (containsPosition(closedList, successor.coordinate) != 0 || !isWalkable(successor.getCoordinate()))
                continue;

            int distanceWeight = 5;
            //Magic happens here
            double tentativeFx = XYsupport.distanceInSteps(successor.getCoordinate(), destination) * distanceWeight + nodeWeight(successor.getCoordinate()) ;
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

    public boolean isWalkable(XY coordinate) {

        EntityType entityTypeAtNewField;
        if(!XYsupport.isInRange(coordinate, XY.ZERO_ZERO, botCom.getFieldLimit()))
            return false;

        try {
            entityTypeAtNewField = context.getEntityAt(coordinate);
        } catch (OutOfViewException e) {
            return true;
        }

        if(!XYsupport.isInRange(coordinate, context.getViewUpperLeft().plus(XY.LEFT_UP), context.getViewLowerRight().plus(XY.RIGHT_DOWN)))
            return false;
        try {
            if (context.getEntityAt(context.locate()) == EntityType.MINISQUIRREL) {
                if (context.getEntityAt(coordinate) == EntityType.MASTERSQUIRREL)
                    return context.isMine(coordinate);
                else
                    return context.getEntityAt(coordinate) != EntityType.MINISQUIRREL
                            && entityTypeAtNewField != EntityType.WALL
                            && entityTypeAtNewField != EntityType.BADBEAST
                            && entityTypeAtNewField != EntityType.BADPLANT;
            }

        } catch (OutOfViewException e) {
            return true;
        }
        return entityTypeAtNewField != EntityType.WALL
                && entityTypeAtNewField != EntityType.BADBEAST
                && entityTypeAtNewField != EntityType.BADPLANT
                && entityTypeAtNewField != EntityType.MASTERSQUIRREL;
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

    private double nodeWeight(XY position){

        try {
            switch (context.getEntityAt(position)){
                case BADPLANT:
                    return BadPlant.START_ENERGY;
                case GOODBEAST:
                    return -GoodBeast.START_ENERGY + checkAdjacentBadBeast(position);
                case GOODPLANT:
                    return -GoodPlant.START_ENERGY + checkAdjacentBadBeast(position);
                case NONE:
                    return checkAdjacentBadBeast(position);
            }
        } catch (OutOfViewException e) {
            return 0;
        }

        return 0;
    }

    private double checkAdjacentBadBeast(XY position){
        double cumulatedWeight = 0;
        for(XY direction : XYsupport.directions()){
            try {
                if(context.getEntityAt(position.plus(direction)) == EntityType.BADBEAST)
                    cumulatedWeight = cumulatedWeight + BadPlant.START_ENERGY;
            } catch (OutOfViewException e) {
                //Do nothing
            }
        }

        return cumulatedWeight;
    }

}

