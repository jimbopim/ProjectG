package com.jimbodev.jimla.projectg;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

class Stationary extends Vector {
    private static Resources resources;
    Bitmap bitmap;
    private float width, height;
    private boolean removable = false;
    int realWidth = 64, realHeight = 64;
    int sizeX = realWidth * 1;
    int sizeY = realHeight * 1;
    private int sheetW = 659, sheetH = 359;
    private int layer1Col = 0, layer1Row = 0;
    Rect layer1Source, dest;
    private int layer1SourceX = realWidth * layer1Col, layer1SourceY = realHeight * layer1Row;

    Stationary(float x, float y, float width, float height) {
        super(x, y);
        this.width = width;
        this.height = height;

        createBitmap(resources);

        layer1Source = new Rect(layer1SourceX, layer1SourceY, layer1SourceX + realWidth, layer1SourceY + realHeight);
        dest = new Rect((int) x, (int) y, (int) x + sizeX, (int) y + sizeY); //TODO * ratio
    }

    static void setResources(Resources resources) {
        Stationary.resources = resources;
    }

    private void createBitmap(Resources resources) {
        bitmap = BitmapFactory.decodeResource(
                resources,
                R.drawable.objects_table
        );
        bitmap = Bitmap.createScaledBitmap(bitmap, sheetW, sheetH, false);
    }

    float getWidth() {
        return width;
    }

    float getHeight() {
        return height;
    }

    float getCenterX() {
        return x + (sizeX/2f);
    }

    float getCenterY() {
        return y + (sizeY/2f);
    }

    boolean isRemovable() {
        return removable;
    }

    void setRemovable(boolean removable) {
        this.removable = removable;
    }

    boolean checkCollision(Vector v, float size) {
        //if(getWidth() == getHeight())
            return Math.hypot(v.x - x, v.y - y) < getWidth() + size;
        //else
            //return ; //TODO Lägg till
    }

    void show(Canvas canvas) {
        canvas.drawBitmap(bitmap, layer1Source, dest, null);
    }
}
