package com.jimbodev.jimla.projectg;

import android.graphics.Canvas;
import android.graphics.Paint;

class Projectile extends Mobile {
    private Attacker parent;
    private int damage;

    Projectile(float x, float y, Vector target, Attacker parent, int damage) {
        super(x, y, 10, 5, 50, 5.0f, target);
        this.parent = parent;
        this.damage = damage;
    }

    void hit() {
        if(parent != null)
            parent.targetHit();
    }

    int getDamage() {
        return damage;
    }

    @Override
    void show(Canvas canvas) {
        canvas.drawCircle(x, y, getWidth(), new Paint());
    }
}
