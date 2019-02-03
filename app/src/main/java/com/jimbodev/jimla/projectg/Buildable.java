package com.jimbodev.jimla.projectg;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;

class Buildable extends Stationary implements Attacker{
    private Rect layer2Source;
    private int layer2Col = 0, layer2Row = 1;
    private int layer2SourceX = realWidth * layer2Col, layer2SourceY = realHeight * layer2Row;
    private double barrelAngle = 0;
    private boolean cooldown = false;

    Buildable(float x, float y) {
        super(x, y, 64, 64);

        layer2Source = new Rect(layer2SourceX, layer2SourceY, layer2SourceX + realWidth, layer2SourceY + realHeight);
    }

    void update(Vector target) {
        if(target != null)
            barrelAngle = getBarrelAngle(target);
    }

    @Override
    public void shoot(Vector target, ArrayList<Projectile> projectiles) {
        if (!cooldown && target != null) {
            projectiles.add(new Projectile(getCenterX(), getCenterY(), target, this, 35));
            cooldown = true;
            Game.HANDLER.postDelayed(coolDownCounter, 1000);
        }
    }

    private Runnable coolDownCounter = new Runnable() {
        @Override
        public void run() {
            cooldown = false;
        }
    };

    @Override
    void show(Canvas canvas) {
        super.show(canvas);
        canvas.save();

        canvas.rotate((float) barrelAngle, getCenterX(), getCenterY());
        canvas.drawBitmap(bitmap, layer2Source, dest, null);

        canvas.restore();
    }

    private double getBarrelAngle(Vector target) {
        double angle = Math.atan2(getCenterY() - target.y, getCenterX() - target.x) - Math.PI / 2;
        angle += Math.toRadians(-90);
        angle = Math.toDegrees(angle);
        return angle;
    }

    @Override
    public void targetHit() {

    }
}
