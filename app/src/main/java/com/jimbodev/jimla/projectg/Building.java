package com.jimbodev.jimla.projectg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

abstract class Building extends Stationary{
    Rect layer2Source;
    int layer2Col;
    int layer2Row;
    int layer2SourceX, layer2SourceY;
    double layer2Angle = 0;

    Building(float x, float y, float scale, int []type) {
        super(x, y, (int)(64 * scale), (int)(64 * scale), type);
        this.layer2Col = type[ObjectType.LAYER2COL];
        this.layer2Row = type[ObjectType.LAYER2ROW];

        layer2SourceX = realWidth * layer2Col;
        layer2SourceY = realHeight * layer2Row;

        layer2Source = new Rect(layer2SourceX, layer2SourceY, layer2SourceX + realWidth, layer2SourceY + realHeight);
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