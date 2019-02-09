package com.jimbodev.jimla.projectg;

//Version 1.7

import android.util.Log;

import java.util.Random;

class Vector {
    protected float x, y;
    Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    void add(Vector vector) {
        if (vector != null) {
            this.x += vector.x;
            this.y += vector.y;
        }
    }

    void add(float value) {
        this.x += value;
        this.y += value;
    }

    void sub(Vector value) {
        this.x -= value.x;
        this.y -= value.y;
    }

    void div(float value) {
        if(value == 0) {
            //Log.e("hejsan", "DIVISION BY ZERO, VECTOR CLASS DIV METHOD");
            return;
        }
        this.x /= value;
        this.y /= value;
    }

    void mult(float value) {
        this.x *= value;
        this.y *= value;
    }

    void normalize() {
        float magnitude = getMag();
        if(magnitude == 0) {
            //Log.e("hejsan", "DIVISION BY ZERO, VECTOR CLASS NORMALIZE METHOD");
            return;
        }
        this.x /= magnitude;
        this.y /= magnitude;
    }

    void rotate(double value) {

        float mag = getMag();
        double angle = getAngle();
        double radians = Math.toRadians(value);
        angle += radians;

        this.x = (float) (mag * -Math.cos(angle));
        this.y = (float) (mag * -Math.sin(angle));
    }

    void limit (float limit) {
        if(getMag() > limit) {
            setMag(limit);
        }
    }

    void setMag(float value) {
        normalize();
        mult(value);
    }

    float getMag() {
        return (float) Math.hypot(this.x, this.y);
    }

    private double getAngle() {
        double angle = Math.atan2(y, x) - Math.PI / 2;
        angle += Math.toRadians(-90);

        return angle;
    }

    void setRandomVelocity(int minAngle, int maxAngle) {
        maxAngle = maxAngle + 1;
        Random r = new Random();
        int random = r.nextInt(maxAngle - minAngle) + minAngle;
        double radians = Math.toRadians(random);

        this.x = (float) Math.cos(radians);
        this.y = (float) Math.sin(radians);
    }

    void setRandomMag(float minSpeed, float maxSpeed) {
        Random r = new Random();
        float mag = (minSpeed + r.nextFloat() * (maxSpeed - minSpeed));
        setMag(mag);
    }

    @Override
    public String toString() {
        return "" + getMag();
    }
}