package com.xtel.ivipbusiness.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Lê Công Long Vũ on 11/4/2016
 */

public class RectangleLayout extends FrameLayout {

    public RectangleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        int width = getMeasuredWidth();
        int percent = width / 16;
        int height = percent * 9;

        setMeasuredDimension(width, height);
    }
}
