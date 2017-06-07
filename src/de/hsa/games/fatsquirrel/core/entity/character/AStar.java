package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

import java.util.*;


/**
 * Created by tillm on 02.06.2017.
 */
public class AStar {
    private List<Node> openList;
    private List<Node> closedList;

    private static class Node {
        private final XY coordinate;
        private double fx;
        private Node successor;
        private Node predecessor;

        Node(XY coordinate) {
            this.coordinate = coordinate;
        }

        public XY getCoordinate() {
            return coordinate;
        }

        public double getFx() {
            return fx;
        }

        public void setFx(double fx) {
            this.fx = fx;
        }

        public Node getSuccessor() {
            return successor;
        }

        public void setSuccessor(Node successor) {
            this.successor = successor;
        }

        public Node getPredecessor() {
            return predecessor;
        }

        public void setPredecessor(Node predecessor) {
            this.predecessor = predecessor;
        }
    }

    public XY directionTo(XY from, XY destination, EntityContext context) {
        openList = new ArrayList<>();
        closedList = new ArrayList<>();

        openList.add(new Node(from));

        while (!openList.isEmpty()) {
            Node currentNode = popMinF(openList);
            if (currentNode.getCoordinate().equals(destination))
                return getSecondNode(currentNode).coordinate.minus(from);

            closedList.add(currentNode);
            expandNode(currentNode, context, destination);
        }

        return null;
    }

    private void expandNode(Node currentNode, EntityContext context, XY destination) {
        for (XY xy : XYsupport.directions()) {
            Node successor = new Node(currentNode.getCoordinate().plus(xy));
            if (containsPosition(closedList, successor.coordinate) != 0 || !isWalkable(successor.getCoordinate(), context))
                continue;

            double tentativeFx = XYsupport.distanceInSteps(successor.getCoordinate(), destination);
            successor.setFx(tentativeFx);

            int position = containsPosition(openList, successor.coordinate);
            if(position != 0){
                if(openList.get(position).getFx() > tentativeFx){
                    openList.remove(position);
                    openList.add(successor);
                }
            }
            else
                openList.add(successor);

            currentNode.setSuccessor(successor);
            successor.setPredecessor(currentNode);
        }
    }

    //TODO: EnemySquirrel mit einebeziehen
    private boolean isWalkable(XY coordinate, EntityContext context){
        EntityType entityTypeAtNewField = context.getEntityType(coordinate);
        return entityTypeAtNewField != EntityType.WALL && entityTypeAtNewField != EntityType.BADBEAST;
    }

    private Node popMinF(List<Node> openList){
        Node min = null;
        for(Node n: openList){
            if(min == null)
                min = n;
            else if(n.getFx() < min.getFx())
                min = n;
        }
        openList.remove(min);

        return min;
    }

    private int containsPosition(List<Node> openList, XY position){
        for(Node n: openList){
            if(n.coordinate.equals(position))
                return openList.indexOf(n);
        }

        return 0;
    }

    private Node getSecondNode(Node lastNode){
        Node predecessor = lastNode;
        while (predecessor.getPredecessor().getPredecessor() != null){
            predecessor = predecessor.getPredecessor();
        }

        return predecessor;
    }


}

