package com.xtel.ivipbusiness.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;

/**
 * Created by Vulcl on 1/18/2017
 */

public class DiagonalLayout extends LinearLayout {

    public DiagonalLayout(Context context) {
        super(context);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(width / 3 + width / 10, 0);
        path.lineTo(width / 3 - width / 10, height);
        path.lineTo(0, height);
        path.close();
        canvas.save();
        canvas.clipPath(path, Region.Op.INTERSECT);
        canvas.drawColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
        canvas.restore();
        path = new Path();
        path.moveTo(width / 3 + width / 10 + width / 10, 0);
        path.lineTo(width, 0);
        path.lineTo(width, height);
        path.lineTo(width / 3, height);
        path.close();
        canvas.save();
        canvas.clipPath(path, Region.Op.INTERSECT);
        Paint paint = new Paint();
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(ContextCompat.getColor(getContext(), android.R.color.black));
        canvas.drawPath(path, paint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }
}