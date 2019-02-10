package com.jimbodev.jimla.projectg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

class SimpleAttacker extends Building implements Attacker{
    private int range = 600;
    private boolean cooldown = false;
    private int cooldownTimer = 5000;
    private int fireAnimationTimer = cooldownTimer / 5;
    private TimeTrackerHandler animations;
    private TimeTrackerHandler.TimeTracker fire;

    int frames = 3;
    int currentFrame = 0;

    SimpleAttacker(float x, float y, float scale, int []type) {
        super(x, y, scale, type);

        animations = new TimeTrackerHandler();
        fire = animations.createNewTimeTracker(fireAnimationTimer);
    }

    void updateFrame(int timeLeft) {
        for (int i = 1; i <= frames; i++) {
            if(timeLeft > (fireAnimationTimer/frames) * (i - 1) && timeLeft < (fireAnimationTimer/frames) * i)
                currentFrame = i - 1;
        }

        setLayer2Source(currentFrame);
    }

    /*void updateFrame(int timeLeft) {
        for (int i = 1; i <= frames; i++) {
            if(timeLeft > (fireAnimationTimer/frames) * (i - 1) && timeLeft < (fireAnimationTimer/frames) * i)
                currentFrame = i - 1;
        }

        setLayer2Source(currentFrame);
    }*/

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

        int timeLeft = animations.getTimeLeft(fire);

        if (animations.isPlaying(fire)) {
            float sin = (float) Math.sin(Math.toRadians(timeLeft * (180f / fireAnimationTimer)));
            canvas.translate(-20 * sin, 0);

            updateFrame(timeLeft);
        }

        drawLayer2(canvas);
        canvas.restore();
        Paint paint = new Paint();
        paint.setTextSize(50);
        canvas.drawText("Fired: " + timeLeft, 100, Game.HEIGHT - 10, paint);
    }
}
