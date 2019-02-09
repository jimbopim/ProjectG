package com.jimbodev.jimla.projectg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

public class ConstructionMenu {
    private boolean active = false;
    private Rect bounds;
    private int windowHeight = 500;
    private ArrayList<Buildable> buildables = new ArrayList<>();
    private Buildable picked = null;
    private Rect icon1, dest;
    public ConstructionMenu(Buildable b) {
        bounds = new Rect(0, Game.HEIGHT - windowHeight, Game.WIDTH, Game.HEIGHT);
        buildables.add(b);
        icon1 = new Rect(30, bounds.top + 30, 30 + buildables.get(0).getWidth(), bounds.top + 30 + buildables.get(0).getHeight()); //Todo Ändra lite
        dest = new Rect(0,0,0,0);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private void showInteractiveIcons(Canvas canvas) {
        Buildable i = buildables.get(0);
        canvas.drawBitmap(i.bitmap, i.layer1Source, icon1, null);
        canvas.drawBitmap(i.bitmap, i.layer2Source, icon1, null);
    }

    private void showPickedIcon(Canvas canvas) {
        Buildable i = buildables.get(0);
        canvas.drawBitmap(i.bitmap, i.layer1Source, dest, null);
        canvas.drawBitmap(i.bitmap, i.layer2Source, dest, null);
    }

    Buildable actionUp(MotionEvent event) {
        if (picked != null) {
            picked = null;
            return new Cannon(event.getX(), event.getY(), 2);
        }
        else {
            return null;
        }
    }

    void actionDown(MotionEvent event) {
        if(event.getX() > icon1.left && event.getX() < icon1.right && event.getY() > icon1.top && event.getY() < icon1.bottom)
            picked = buildables.get(0);
    }

    void actionMove(MotionEvent event) {
        if (picked != null) {
            dest.left = (int) event.getX();
            dest.top = (int) event.getY();
            dest.right = dest.left + picked.getWidth();
            dest.bottom = dest.top + picked.getHeight();
        }
    }

    void show(Canvas canvas) {
        if (isActive()) {
            Paint paint = new Paint();
            paint.setARGB(125, 0, 0 ,0);

            if (picked != null) {
                showPickedIcon(canvas);
            }
            else {
                canvas.drawRect(bounds, paint);
                showInteractiveIcons(canvas);
            }
        }
    }
}
