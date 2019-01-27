package com.jimbodev.jimla.projectg;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

class Buildable extends Vector {
    private Bitmap bitmap;
    private Rect source, dest;
    private int sourceX = 1, sourceY = 3;
    private int w = 168;
    Buildable(float x, float y, Resources resources) {
        super(x, y);

        bitmap = BitmapFactory.decodeResource(
                resources,
                R.drawable.table2
        );

        source = new Rect(sourceX, sourceY, sourceX + w, sourceY + w);
        dest = new Rect((int) x, (int) y, (int) x + w, (int) y + w); //TODO * ratio
        //dest = new Rect((int) x, (int) y, (int) x + w * 2, (int) y + w * 2); //TODO * ratio
    }

    void show(Canvas canvas) {
        canvas.drawBitmap(bitmap, source, dest, null);
    }
}
