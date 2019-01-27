package com.jimbodev.jimla.projectg;

import android.util.Log;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

class Boid extends Vector {
    private Vector velocity, acceleration;
    private float maxSpeed = 10.0f, maxForce = 5.0f, size;

    private int perceptionRadius = 30;

    private Pathfinder pathfinder;
    private Node startNode, goalNode, nextTargetNode;
    private ListIterator<Node> iter;

    Boid(float x, float y, float size, Node startNode, Node goalNode) {
        super(x, y);
        this.size = size;
        velocity = new Vector(0, 0);
        acceleration = new Vector(0, 0);

        Random r = new Random();
        maxSpeed = r.nextInt(10) + 2;

        this.startNode = startNode;
        this.goalNode = goalNode;
        this.nextTargetNode = startNode;


        pathfinder = new Pathfinder(startNode, goalNode);
        pathfinder.update(false);
        iter = pathfinder.getTotal_path().listIterator();
    }

    void update(ArrayList<Boid> boids) {
        float distance = 0;
        int minDistance = 50;
        if (nextTargetNode != null) {
            distance = (float) Math.hypot(x - nextTargetNode.getRealX(), y - nextTargetNode.getRealY());

            acceleration.add(moveToTarget(nextTargetNode));

            if (!iter.hasNext()) {
                if (distance < 5) {
                    Log.i("hejsan", "No more target");
                    nextTargetNode = null;
                }
            }
            else {
                if (distance < minDistance) { //maxSpeed
                    nextTargetNode = iter.next();
                }
            }
        }

        steering(boids);

        velocity.add(acceleration);
        velocity.limit(maxSpeed);

        float dis = maxSpeed * 10f; //100
        if (remainingDistanceF() <= dis && velocity.getMag() > 0) {
            slowDownVelocity(distance, dis);
        }

        add(velocity);
        acceleration.mult(0);
    }

    private void slowDownVelocity(float distance, float dis) {
        velocity.setMag(maxSpeed);
        float sin = (float) Math.sin(Math.toRadians(remainingDistanceF() * (90f / dis)));

        //Log.i("hejsan", "remainingDistance: " + remainingDistanceF() + " sin: " + sin + " velocity: " + velocity.getMag());

        if (distance < 1) {
            sin = 0;
            x = goalNode.getRealX();
            y = goalNode.getRealY();
        }
        else if (sin < 0.1f)
            sin = 0.1f;

        velocity.setMag(velocity.getMag() * sin);
    }

    private int remainingDistance() {
        if (nextTargetNode != null)
            return pathfinder.getTotal_path().size() - pathfinder.getTotal_path().indexOf(nextTargetNode);
        else
            return 0;
    }

    private float remainingDistanceF() {
        return (float) Math.hypot(goalNode.getRealX() - x, goalNode.getRealY() - y);
    }


    private Vector moveToTarget(Node next) {
        return target(new Vector(next.getRealX(), next.getRealY()));
    }

    private Vector target(Vector screenPos) {
        Vector steering = new Vector(0, 0);
        steering.add(screenPos);
        steering.sub(this);
        steering.setMag(maxSpeed);
        //steering.sub(velocity);
        steering.limit(maxForce);
        return steering;
    }

    private void steering(ArrayList<Boid> boids) {
        Vector steeringSeperation = new Vector(0, 0);
        int total = 0;

        for (Boid b : boids) {
            double distance = Math.hypot(x - b.x, y - b.y);
            if (distance < perceptionRadius && b != this) {
                Vector diff = new Vector(0, 0);
                diff.add(this);
                diff.sub(b);
                diff.div((float) (distance * distance));
                steeringSeperation.add(diff);

                total++;
            }
        }
        if (total > 0) {
            steeringSeperation.div(total);
            steeringSeperation.setMag(maxSpeed);
            steeringSeperation.sub(velocity);
            steeringSeperation.limit(maxForce);
        }
        acceleration.add(steeringSeperation);
    }

    float getSize() {
        return size;
    }

    float getSpeed() {
        return (float) Math.hypot(velocity.x, velocity.y);
    }
}
