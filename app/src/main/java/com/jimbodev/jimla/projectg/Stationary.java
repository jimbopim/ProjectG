package com.jimbodev.jimla.projectg;

public class Stationary extends Vector {
    private float size;
    private boolean active = true;

    Stationary(float x, float y, float size) {
        super(x, y);
        this.size = size;
    }

    float getSize() {
        return size;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    boolean checkCollision(Vector v, float size) {
        return Math.hypot(v.x - x, v.y - y) < getSize() + size;
    }
}
