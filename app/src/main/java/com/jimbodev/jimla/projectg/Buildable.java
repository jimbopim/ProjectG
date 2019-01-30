package com.jimbodev.jimla.projectg;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;

class Buildable extends Vector {
    private Bitmap bitmap;
    private Rect baseSource, weaponSource, dest;
    private int realWidth = 64, realHeight = 64;
    private int sizeX = realWidth * 5;
    private int sizeY = realHeight * 5;
    private int baseSourceX = realWidth * 0, baseSourceY = realHeight * 0;
    private int weaponSourceX = realWidth * 0, weaponSourceY = realHeight * 1;
    private int sheetW = 659, sheetH = 359;
    private double barrelAngle = 0;
    private boolean cooldown = false;

    Buildable(float x, float y, Resources resources) {
        super(x, y);

        bitmap = BitmapFactory.decodeResource(
                resources,
                R.drawable.objects_table
        );
        bitmap = Bitmap.createScaledBitmap(bitmap, sheetW, sheetH, false);

        baseSource = new Rect(baseSourceX, baseSourceY, baseSourceX + realWidth, baseSourceY + realHeight);
        weaponSource = new Rect(weaponSourceX, weaponSourceY, weaponSourceX + realWidth, weaponSourceY + realHeight);
        dest = new Rect((int) x, (int) y, (int) x + sizeX, (int) y + sizeY); //TODO * ratio
    }

    void update(Vector target, ArrayList<Projectile> projectiles) {
        if(target != null)
            barrelAngle = getBarrelAngle(target);

        if(!cooldown && target != null)
            shoot(target, projectiles);
    }

    private void shoot(Vector target, ArrayList<Projectile> projectiles) {
        projectiles.add(new Projectile(getCenterX(), getCenterY(), target, this));
        cooldown = true;
        Game.HANDLER.postDelayed(coolDownCounter, 1000);
    }

    Runnable coolDownCounter = new Runnable() {
        public void run() {
            cooldown = false;
        }
    };

    void show(Canvas canvas) {
        canvas.drawBitmap(bitmap, baseSource, dest, null);

        canvas.save();

        canvas.rotate((float) barrelAngle, getCenterX(), getCenterY());
        canvas.drawBitmap(bitmap, weaponSource, dest, null);

        canvas.restore();
    }

    private double getBarrelAngle(Vector target) {
        double angle = Math.atan2(getCenterY() - target.y, getCenterX() - target.x) - Math.PI / 2;
        angle += Math.toRadians(-90);
        angle = Math.toDegrees(angle);
        return angle;
    }

    private float getCenterX() {
        return x + (sizeX/2f);
    }

    private float getCenterY() {
        return y + (sizeY/2f);
    }
}
