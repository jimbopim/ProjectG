package com.jimbodev.jimla.projectg;

import android.util.Log;

import java.util.ArrayList;

public class Projectile extends Vector {
    private Vector velocity, acceleration;
    private float maxSpeed = 10.0f, maxForce = 5.0f, size;
    Buildable parent;
    Vector target;
    Projectile(float x, float y, Vector target, Buildable parent) {
        super(x, y);
        velocity = new Vector(0, 0);
        acceleration = new Vector(0, 0);
        this.parent = parent;
        this.target = target;
    }

    void update() {

        acceleration.add(moveToTarget());
        velocity.add(acceleration);
        velocity.limit(maxSpeed);

        add(velocity);
        acceleration.mult(0);
    }

    private Vector moveToTarget() {
        return target(new Vector(target.x, target.y));
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
}
