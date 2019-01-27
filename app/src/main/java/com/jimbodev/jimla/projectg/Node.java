package com.jimbodev.jimla.projectg;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

class Node {
    int x, y;
    private float realX, realY;
    private ArrayList<Node> neighbours = new ArrayList<>();
    private int gScore; //Todo Hitta på något bättre
    private float fScore; //Todo Hitta på något bättre
    Node cameFrom = null;
    private boolean active = true;

    Node(int x, int y, float realX, float realY) {
        this.x = x;
        this.y = y;
        this.realX = realX;
        this.realY = realY;
    }

    float getRealX() { return realX; }

    float getRealY() { return realY; }

    int getgScore() { return gScore; }

    void setgScore(int gScore) { this.gScore = gScore; }

    float getfScore() { return fScore; }

    void setfScore(float fScore) { this.fScore = fScore; }

    boolean isActive() { return active; }

    void setActive(boolean active) { this.active = active; }

    ArrayList<Node> getNeighbours() { return neighbours; }

    void connectNeighbours(ArrayList<ArrayList<Node>> nodes) {

        neighbours.clear();

        //Vertikalt och horisontalt
        if (x - 1 >= 0 && nodes.get(x - 1).get(y).isActive())
            neighbours.add(nodes.get(x - 1).get(y));
        if (x + 1 < Game.cols && nodes.get(x + 1).get(y).isActive())
            neighbours.add(nodes.get(x + 1).get(y));
        if (y - 1 >= 0 && nodes.get(x).get(y - 1).isActive())
            neighbours.add(nodes.get(x).get(y - 1));
        if (y + 1 < Game.rows && nodes.get(x).get(y + 1).isActive())
            neighbours.add(nodes.get(x).get(y + 1));

        //Diagonalt
        if (x - 1 >= 0 && y - 1 >= 0 && nodes.get(x - 1).get(y - 1).isActive())
            neighbours.add(nodes.get(x - 1).get(y - 1));
        if (x + 1 < Game.cols && y - 1 >= 0 && nodes.get(x + 1).get(y - 1).isActive())
            neighbours.add(nodes.get(x + 1).get(y - 1));
        if (x - 1 >= 0 && y + 1 < Game.rows && nodes.get(x - 1).get(y + 1).isActive())
            neighbours.add(nodes.get(x - 1).get(y + 1));
        if (x + 1 < Game.cols && y + 1 < Game.rows && nodes.get(x + 1).get(y + 1).isActive())
            neighbours.add(nodes.get(x + 1).get(y + 1));
    }

    void show(Canvas canvas, Paint paint) {
        if (active)
            canvas.drawCircle(realX, realY, Game.size, paint);
    }
}
