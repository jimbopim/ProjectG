package com.jimbodev.jimla.projectg;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

public class Cannon extends Buildable implements Attacker{
    private int range = 600;
    private boolean cooldown = false;
    private int cooldownTimer = 1000;
    private int fireAnimationTimer = cooldownTimer / 5;
    private TimeTrackerHandler animations;
    private TimeTrackerHandler.TimeTracker fire;
    Cannon(float x, float y, float scale) {
        super(x, y, scale);

        animations = new TimeTrackerHandler();
        fire = animations.createNewTimeTracker(fireAnimationTimer);
    }

    @Override
    public void targetHit() {

    }

    @Override
    public void shoot(Vector target, ArrayList<Projectile> projectiles) {
        if (canShoot(target)) {
            projectiles.add(new Cannonball(getCenterX(), getCenterY(), target, this, 35));
            cooldown = true;
            animations.play(fire);
            Game.HANDLER.postDelayed(resetCooldown, cooldownTimer);
        }
    }

    private boolean canShoot(Vector target) {
        return (!cooldown && target != null && Math.hypot(getCenterX() - target.x, getCenterY() - target.y) <= range);
    }

    private Runnable resetCooldown = new Runnable() {
        @Override
        public void run() {
            cooldown = false;
        }
    };

    @Override
    void show(Canvas canvas) {
        drawLayer1(canvas);

        canvas.save();
        rotateLayer2AgainstTarget(canvas);

        if (animations.isPlaying(fire)) {
            float sin = (float) Math.sin(Math.toRadians(animations.getTimeLeft(fire) * (180f / fireAnimationTimer)));
            canvas.translate(-20 * sin, 0);
        }

        canvas.drawBitmap(bitmap, layer2Source, dest, null);
        canvas.restore();
        Paint paint = new Paint();
        paint.setTextSize(50);
        canvas.drawText("Fired: " + animations.getTimeLeft(fire), 100, Game.HEIGHT - 10, paint);
    }
}
