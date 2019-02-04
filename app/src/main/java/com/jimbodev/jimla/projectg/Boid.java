package com.jimbodev.jimla.projectg;

import android.util.Log;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

class Boid extends Mobile implements Runner{
    private int perceptionRadius = 30;

    private Pathfinder pathfinder;
    private Node nextTargetNode, goalNode;
    private ListIterator<Node> iter;

    private int health = 100;

    Boid(float x, float y, int size, Node startNode, Node goalNode) {
        super(x, y, size, size, new Random().nextInt(10) + 2, 5, null);

        this.nextTargetNode = startNode;
        this.goalNode = goalNode;


        pathfinder = new Pathfinder(startNode, goalNode);
        pathfinder.update(false);
        iter = pathfinder.getTotal_path().listIterator();
    }

    void update(ArrayList<Boid> boids) {

        float distance = getDistance();
        acceleration.add(getTargetPosition(new Vector(nextTargetNode.getRealX(), nextTargetNode.getRealY())));
        updateNextTargetNode(distance);

        acceleration.add(steering(boids));

        velocity.add(acceleration);
        velocity.limit(maxSpeed);

        float dis = maxSpeed * 10f; //100
        if (remainingDistanceF() <= dis && velocity.getMag() > 0) {
            slowDownVelocity(distance, dis);
        }

        add(velocity);
        acceleration.mult(0);
    }

    private void updateNextTargetNode(float distance) {
        int minDistance = 50;
        if (nextTargetNode != null) {
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
    }

    private float getDistance() {
        float distance = 0;
        if (nextTargetNode != null) {
            distance = (float) Math.hypot(x - nextTargetNode.getRealX(), y - nextTargetNode.getRealY());
        }
        return distance;
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

    private Vector steering(ArrayList<Boid> boids) {
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
        return steeringSeperation;
    }

    void changeHealth(int change) {
        health += change;
        if (health <= 0) {
            health = 0;
            setRemovable(true);
        }
    }

    float getSpeed() {
        return (float) Math.hypot(velocity.x, velocity.y);
    }

    @Override
    public void hit(Projectile projectile) {
        changeHealth(-projectile.getDamage());
    }
}
