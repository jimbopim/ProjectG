package com.jimbodev.jimla.projectg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

class SimpleAttacker extends Building implements Attacker{
    private int range = 600;
    private boolean cooldown = false;
    private int cooldownTimer = 5000;
    private int fireAnimationTimer = cooldownTimer / 5;
    private TimeTrackerHandler animations;
    private TimeTrackerHandler.TimeTracker fire;

    int frames;
    int currentFrame = 0;

    SimpleAttacker(float x, float y, ObjectType.Tower type) {
        super(x, y, (ObjectType.Bitmap) type);

        animations = new TimeTrackerHandler();
        fire = animations.createNewTimeTracker(fireAnimationTimer);

        frames = type.getFireframes();
    }

    private void updateAnimation(Canvas canvas) {
        int timeLeft = animations.getTimeLeft(fire);

        if (animations.isPlaying(fire)) {
            float sin = (float) Math.sin(Math.toRadians(timeLeft * (180f / fireAnimationTimer)));
            canvas.translate(-20 * sin, 0);

            updateFrame(timeLeft);
        }
        else
            setLayer2Source(currentFrame = 0);
    }

    void updateFrame(int timeLeft) {
        int j = 1;
        for (int i = frames; i > 0; i--) {
            if (timeLeft > (fireAnimationTimer / frames) * (i - 1) && timeLeft < (fireAnimationTimer / frames) * i) {
                currentFrame = j;
                Log.i("hejsan", "TimeLeft: " + timeLeft + " currentFrame: " + currentFrame);
                break;
            }
            else
                j++;
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

        updateAnimation(canvas);

        drawLayer2(canvas);
        canvas.restore();

        Paint paint = new Paint();
        paint.setTextSize(50);
        canvas.drawText("Fired: " + animations.getTimeLeft(fire), 100, Game.HEIGHT - 10, paint);
    }
}
