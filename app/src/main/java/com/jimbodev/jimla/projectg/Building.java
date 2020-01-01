package com.jimbodev.jimla.projectg;

import android.graphics.Canvas;
import android.graphics.Rect;

abstract class Building extends Stationary {
    Rect layer2Source;
    int []layer2Coord;
    int layer2SourceX, layer2SourceY;
    double layer2Angle = 0;

    Building(float x, float y, ObjectType.Bitmap type) {
        super(x, y, type);
        this.layer2Coord = type.getLayer2Coord();

        setLayer2Source(0, type);
    }

    void setLayer2Source(int frameCount, ObjectType.Bitmap type) {
        int frame = frameCount * ObjectType.FRAMEHEIGHT;
        layer2SourceX = type.getLayer2Coord()[0] + (type.getLayer2Coord()[0] * ObjectType.FRAMEWIDTH);
        layer2SourceY = type.getLayer2Coord()[1] + (type.getLayer2Coord()[1] * ObjectType.FRAMEHEIGHT + frame);

        layer2Source = new Rect(layer2SourceX, layer2SourceY, layer2SourceX + type.getSourceSizeX(), layer2SourceY + type.getSourceSizeY());
    }

    void update(Vector target) {
        if(target != null)
            layer2Angle = getLayer2Angle(target);
    }

    private double getLayer2Angle(Vector target) {
        double angle = Math.atan2(getCenterY() - target.y, getCenterX() - target.x) - Math.PI / 2;
        angle += Math.toRadians(-90);
        angle = Math.toDegrees(angle);
        return angle;
    }

    void drawLayer2(Canvas canvas) {
        canvas.drawBitmap(bitmap, layer2Source, dest, null);
    }

    void rotateLayer2AgainstTarget(Canvas canvas) {
        canvas.rotate((float) layer2Angle, getCenterX(), getCenterY());
    }

    @Override
    void show(Canvas canvas) {
        super.show(canvas);

        canvas.save();
        rotateLayer2AgainstTarget(canvas);

        drawLayer2(canvas);
        canvas.restore();
    }
}
