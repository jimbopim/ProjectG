package com.jimbodev.jimla.projectg;

class Cannonball extends Projectile {
    Cannonball(float x, float y, Vector target, Attacker parent, int damage) {
        super(x, y, ObjectType.CANNONBALL, target, parent, damage);

        velocity.add(getTargetPosition(target));
        velocity.setMag(maxSpeed);
    }
}
