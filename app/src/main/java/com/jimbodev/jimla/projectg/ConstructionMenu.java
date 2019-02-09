package com.jimbodev.jimla.projectg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class ConstructionMenu {
    private boolean active = false;
    Rect bounds;
    int windowHeight = 500;
    public ConstructionMenu() {
        bounds = new Rect(0, Game.HEIGHT - windowHeight, Game.WIDTH, Game.HEIGHT);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    void show(Canvas canvas) {
        if (isActive()) {
            Paint paint = new Paint();
            paint.setARGB(125, 0, 0 ,0);
            canvas.drawRect(bounds, paint);
        }
    }
}
