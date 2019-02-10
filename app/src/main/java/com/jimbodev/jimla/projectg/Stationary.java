package com.jimbodev.jimla.projectg;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

class Stationary extends Vector {
    private static Resources resources;
    Bitmap bitmap;
    private boolean removable = false;
    protected int realWidth = 64;
    protected int realHeight = 64;
    int width = realWidth * 1;
    int height = realHeight * 1;
    private int sheetW = 659, sheetH = 359;
    private int layer1Col;
    private int layer1Row;
    Rect layer1Source, dest;
    private int layer1SourceX, layer1SourceY;

    Stationary(float x, float y, int width, int height, int []type) {
        super(x, y);
        this.width = width;
        this.height = height;
        if (type != null) {
            this.layer1Col = type[ObjectType.LAYER1COL];
            this.layer1Row = type[ObjectType.LAYER1ROW];
        }

        setLayer1Source(0);

        createBitmap(resources);


        dest = new Rect((int) x, (int) y, (int) x + this.width, (int) y + this.height); //TODO * ratio
    }

    void setLayer1Source(int frameCount) {
        int row = layer1Row + (frameCount * layer1Row);
        layer1SourceX = (realWidth * layer1Col) + layer1Col + 1;
        layer1SourceY = (realHeight * row) + row + 1;

        layer1Source = new Rect(layer1SourceX, layer1SourceY, layer1SourceX + realWidth, layer1SourceY + realHeight);
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

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    float getCenterX() {
        return x + (width /2f);
    }

    float getCenterY() {
        return y + (height /2f);
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

    void drawLayer1(Canvas canvas) {
        canvas.drawBitmap(bitmap, layer1Source, dest, null);
    }

    void show(Canvas canvas) {
        drawLayer1(canvas);
    }
}
