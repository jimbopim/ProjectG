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
    private int width;
    private int height;
    private int sheetW = 659, sheetH = 359;
    private int [] layer1Coord;
    Rect layer1Source, dest;
    private int layer1SourceX, layer1SourceY;
    protected ObjectType.Drawable type;
    private boolean obstacle = true;

    public boolean isObstacle() {
        return obstacle;
    }

    public void setObstacle(boolean obstacle) {
        this.obstacle = obstacle;
    }

    Stationary(float x, float y, ObjectType.Drawable type) {
        super(x, y);

        setBounds(type);
        setLayer1Source(0, type);
        this.type = type;

        createBitmap(resources);


        dest = new Rect((int) x, (int) y, (int) x + this.width, (int) y + this.height); //TODO * ratio
    }

    void setLayer1Source(int frameCount, ObjectType.Drawable type) {
        if (type instanceof ObjectType.Bitmap) {
            ObjectType.Bitmap type1 = (ObjectType.Bitmap) type;
            int frame = frameCount * ObjectType.FRAMEHEIGHT;
            layer1SourceX = type1.getLayer1Coord()[0] + (type1.getLayer1Coord()[0] * ObjectType.FRAMEWIDTH);
            layer1SourceY = type1.getLayer1Coord()[1] + (type1.getLayer1Coord()[1] * ObjectType.FRAMEHEIGHT + frame);

            layer1Source = new Rect(layer1SourceX, layer1SourceY, layer1SourceX + type1.getSourceSizeX(), layer1SourceY + type1.getSourceSizeY());
        }
    }

    /*void setLayer1Source(int frameCount) {
        if (layer1Coord != null) {
            int row = layer1Coord[1] + (frameCount * layer1Coord[1]);
            layer1SourceX = (realWidth * layer1Coord[0]) + layer1Coord[0] + 1;
            layer1SourceY = (realHeight * row) + row + 1;

            layer1Source = new Rect(layer1SourceX, layer1SourceY, layer1SourceX + realWidth, layer1SourceY + realHeight);
        }
    }*/

    static void setResources(Resources resources) {
        Stationary.resources = resources;
    }

    private void setBounds(ObjectType.Drawable type) {
        if (type instanceof ObjectType.Bitmap) {
            ObjectType.Bitmap type1 = (ObjectType.Bitmap) type;
            this.width = type1.getSourceSizeX() * type1.getScale();
            this.height = type1.getSourceSizeY() * type1.getScale();
            this.layer1Coord = type1.getLayer1Coord();
        }
        else {
            this.width = ((ObjectType.Paintable) type).getSize();
            this.height = ((ObjectType.Paintable) type).getSize();
        }
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
