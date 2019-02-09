package com.jimbodev.jimla.projectg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

class Buildable extends Stationary{
    Rect layer2Source;
    int layer2Col = 0, layer2Row = 1;
    int layer2SourceX = realWidth * layer2Col, layer2SourceY = realHeight * layer2Row;
    double layer2Angle = 0;

    Buildable(float x, float y, float scale) {
        super(x, y, (int)(64 * scale), (int)(64 * scale));

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

    void rotateLayer2AgainstTarget(Canvas canvas) {
        canvas.rotate((float) layer2Angle, getCenterX(), getCenterY());
    }

    @Override
    void show(Canvas canvas) {
        super.show(canvas);

        canvas.save();
        rotateLayer2AgainstTarget(canvas);

        canvas.drawBitmap(bitmap, layer2Source, dest, null);
        canvas.restore();
    }
}
