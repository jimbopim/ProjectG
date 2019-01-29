package com.jimbodev.jimla.projectg;

public class Stationary extends Vector {
    private float size;

    Stationary(float x, float y, float size) {
        super(x, y);
        this.size = size;
    }

    float getSize() {
        return size;
    }
}
