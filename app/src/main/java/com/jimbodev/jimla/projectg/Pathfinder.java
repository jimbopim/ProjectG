package com.jimbodev.jimla.projectg;

import android.util.Log;

import java.util.HashSet;
import java.util.LinkedList;

//Version 1.3

class Pathfinder {

    private Node goalNode;
    private HashSet<Node> closedSet;
    private HashSet<Node> openSet;
    private LinkedList<Node> total_path;

    Pathfinder(Node startNode, Node goalNode) {
        startNode.setfScore(distance(startNode, goalNode)); //TODO Funkar?
        startNode.setgScore(0);
        this.goalNode = goalNode;

        openSet = new HashSet<>();
        closedSet = new HashSet<>();
        total_path = new LinkedList<>();
        openSet.add(startNode);
    }

    boolean update(boolean visualize) {
        return pathfinding(visualize);
    }

    private boolean pathfinding(boolean visualize) {

        while (!openSet.isEmpty()) {

            Node current = null; //LowestFScore
            for (Node n : openSet) {
                if (current == null || n.getfScore() < current.getfScore()) {
                    current = n;
                }
            }

            if (current == goalNode) {
                Log.i("hejsan", "Done!");
                total_path = reconstructPath(current);
                return true;
            }

            openSet.remove(current);
            closedSet.add(current);

            for (Node neighbor : current.getNeighbours()) {
                if (closedSet.contains(neighbor))
                    continue;

                float tentative_gScore = current.getgScore() + distance(current, neighbor);

                if (!openSet.contains(neighbor) && neighbor.isActive())
                    openSet.add(neighbor);
                else if (tentative_gScore >= neighbor.getgScore())
                    continue;

                // This path is the best until now. Record it!
                neighbor.cameFrom = current;
                neighbor.setgScore((int) tentative_gScore);
                neighbor.setfScore(neighbor.getgScore() + distance(neighbor, goalNode));
            }
            if (visualize) {
                total_path = reconstructPath(current);
                break;
            }
        }

        //No solution
        if ((openSet.isEmpty())) {
            Log.i("hejsan", "No solution found!");
            return false;
        }
        return false;
    }

    private LinkedList<Node> reconstructPath(Node c) {
        Node current = c;
        LinkedList<Node> path = new LinkedList<>();
        path.add(current);
        while (current.cameFrom != null) {
            current = current.cameFrom;
            path.push(current);
        }

        return path;
    }

    private float distance(Node n, Node m) {
        if (n == null)
            Log.e("hejsan", "N == null - distance");
        if (m == null)
            Log.e("hejsan", "M == null - distance");
        return (float) Math.hypot(n.x - m.x, n.y - m.y);
    }

    HashSet<Node> getClosedSet() {
        return closedSet;
    }

    HashSet<Node> getOpenSet() {
        return openSet;
    }

    LinkedList<Node> getTotal_path() {
        return total_path;
    }
}
