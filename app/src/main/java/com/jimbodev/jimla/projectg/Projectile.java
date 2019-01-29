package com.jimbodev.jimla.projectg;

class Projectile extends Mobile {
    private Buildable parent;
    Projectile(float x, float y, Vector target, Buildable parent) {
        super(x, y, 20, 10, 5.0f, target);
        this.parent = parent;
    }
}
