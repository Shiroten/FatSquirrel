package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.core.entity.Entity;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

import static de.hsa.games.fatsquirrel.XYsupport.directions;

/**
 * Created by tillm on 02.06.2017.
 */
public class AStar {
    private static class Node {
        private final XY coordinate;
        private int fx;
        private Node successor;

        Node(XY coordinate) {
            this.coordinate = coordinate;
        }

        public XY getCoordinate() {
            return coordinate;
        }

        public int getFx() {
            return fx;
        }

        public void setFx(int fx) {
            this.fx = fx;
        }

        public Node getSuccessor() {
            return successor;
        }

        public void setSuccessor(Node successor) {
            this.successor = successor;
        }
    }

    public static XY directionTo(XY from, XY destination, EntityContext context) {
        TreeSet<Node> openList = new TreeSet<>(Comparator.comparingInt(Node::getFx));

        HashSet<Node> closedList = new HashSet<>();

        openList.add(new Node(from));

        while (!openList.isEmpty()) {
            Node currentNode = openList.pollFirst();
            if (currentNode.getCoordinate() == destination)
                return XY.DOWN;

            closedList.add(currentNode);


        }

        return null;
    }

    private static void expandNode(TreeSet<Node> openList, HashSet<Node> closedList, Node node,
                                   EntityContext context, XY destination) {
        for (XY xy : XYsupport.directions()) {
            Node successor = new Node(node.getCoordinate().plus(xy));
            if (closedList.contains(successor) || !isWalkable(successor.getCoordinate(), context))
                continue;

            double tentativeFx = node.getFx() + XYsupport.distanceInSteps(node.getCoordinate(), destination);

            if(openList.contains(successor)){

            }
        }
    }

    //TODO: EnemySquirrel mit einebeziehen
    private static boolean isWalkable(XY coordinate, EntityContext context){
        EntityType entityTypeAtNewField = context.getEntityType(coordinate);
        return entityTypeAtNewField != EntityType.WALL && entityTypeAtNewField != EntityType.BADBEAST;
    }
}

