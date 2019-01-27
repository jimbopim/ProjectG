package com.jimbodev.jimla.projectg;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

class Buildable extends Vector {
    private Bitmap bitmap;
    private Rect source, dest;
    private int sourceX = 1, sourceY = 1;
    private int w = 64, h = 64;
    Buildable(float x, float y, Resources resources) {
        super(x, y);

        bitmap = BitmapFactory.decodeResource(
                resources,
                R.drawable.objects_table
        );
        //bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        //bitmap = Bitmap.createScaledBitmap(bitmap, w * 8, h, false);

        //source = new Rect(sourceX, sourceY, sourceX + w, sourceY + w);
        //dest = new Rect((int) x, (int) y, (int) x + w, (int) y + w); //TODO * ratio
        source = new Rect(0, 0, w, h);
        dest = new Rect(0, 0, w, h); //TODO * ratio
    }

    void show(Canvas canvas) {
        canvas.drawBitmap(bitmap, source, dest, null);
    }
}
