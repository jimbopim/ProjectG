package com.jimbodev.jimla.projectg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

class ConstructionMenu {
    private boolean active = false;
    private Rect bounds;
    private int windowHeight = 500;
    private ArrayList<Building> buildings = new ArrayList<>();
    private Building picked = null;
    private Rect startIcon, dest;
    private ArrayList<Rect> icons;
    ConstructionMenu(ArrayList<Building> b) {
        bounds = new Rect(0, Game.HEIGHT - windowHeight, Game.WIDTH, Game.HEIGHT);
        buildings.addAll(b);
        startIcon = new Rect(30, bounds.top + 30, 30 + buildings.get(0).getWidth(), bounds.top + 30 + buildings.get(0).getHeight()); //Todo Ändra lite
        dest = new Rect(0,0,0,0);
        icons = new ArrayList<>();
        setupIcons();
    }

    boolean isActive() {
        return active;
    }

    void setActive(boolean active) {
        this.active = active;
    }

    private void setupIcons() {
        int countX = 0, countY = 0, w = 128, h = 128;
        for (Building i : buildings) {
            Rect place = new Rect(startIcon.left + (w * countX), startIcon.top + (h * countY), startIcon.right + (w * countX), startIcon.bottom + (h * countY));
            icons.add(place);
            countX++;
            if (countX > 4) {
                countX = 0;
                countY++;
            }
        }
    }

    private void showInteractiveIcons(Canvas canvas) {
        int i = 0;
        for (Rect d : icons) {
            showIcon(canvas, buildings.get(i), d);
            i++;
        }
    }

    private void showIcon(Canvas canvas, Building b, Rect mDest) {
        canvas.drawBitmap(b.bitmap, b.layer1Source, mDest, null);
        canvas.drawBitmap(b.bitmap, b.layer2Source, mDest, null);
    }

    Building actionUp(MotionEvent event) {
        if (picked != null) {
            Building temp = picked;
            picked = null;
            return getBuilding(temp, event);
        }
        else {
            return null;
        }
    }

    void actionDown(MotionEvent event) {
        int i = 0;
        for (Rect r : icons) {
            if (event.getX() > r.left && event.getX() < r.right && event.getY() > r.top && event.getY() < r.bottom) {
                picked = buildings.get(i);
                break;
            }
            i++;
        }
    }

    void actionMove(MotionEvent event) {
        if (picked != null) {
            dest.left = (int) event.getX();
            dest.top = (int) event.getY();
            dest.right = dest.left + picked.getWidth();
            dest.bottom = dest.top + picked.getHeight();
        }
    }

    private Building getBuilding(Building type, MotionEvent event) {
        if (type instanceof Cannon)
            return new Cannon(event.getX(), event.getY(), 2);
        else
            return new ArrowShooter(event.getX(), event.getY(), 2);
    }

    void show(Canvas canvas) {
        if (isActive()) {
            Paint paint = new Paint();
            paint.setARGB(125, 0, 0 ,0);

            if (picked != null) {
                showIcon(canvas, picked, dest);
            }
            else {
                canvas.drawRect(bounds, paint);
                showInteractiveIcons(canvas);
            }
        }
    }
}
