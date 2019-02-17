package com.jimbodev.jimla.projectg;

class Mobile extends Stationary {
    Vector velocity, acceleration;
    float maxSpeed, maxForce;
    private Vector target;

    Mobile(float x, float y, ObjectType.Paintable type, float maxSpeed, float maxForce, Vector target) {
        super(x, y, type);
        this.maxSpeed = maxSpeed;
        this.maxForce = maxForce;
        this.target = target;

        velocity = new Vector(0, 0);
        acceleration = new Vector(0, 0);
    }

    void update() {

        acceleration.add(getTargetPosition(target));
        velocity.add(acceleration);
        velocity.limit(maxSpeed);

        add(velocity);
        acceleration.mult(0);
    }

    Vector getTargetPosition(Vector target) {
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
