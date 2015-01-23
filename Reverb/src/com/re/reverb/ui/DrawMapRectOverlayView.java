package com.re.reverb.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.re.reverb.R;
import com.re.reverb.ui.shapeWrappers.CircleShape;
import com.re.reverb.ui.shapeWrappers.RectangleShape;
import com.re.reverb.ui.shapeWrappers.Shape;

import java.util.Stack;

public class DrawMapRectOverlayView extends DrawMapShapeOverlayView
{

    public DrawMapRectOverlayView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.currentShape = new RectangleShape();
        this.currentShape.getShapeDrawable().getPaint().setColor(getResources().getColor(R.color.reverb_blue_1));
    }

    public DrawMapRectOverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawMapRectOverlayView(Context context) {
        this(context, null, 0);
    }

    @Override
    protected void onTouchDown()
    {
        if (this.currentShape == null)
        {
            this.currentShape = new RectangleShape();
            this.currentShape.getShapeDrawable().getPaint().setColor(getResources().getColor(R.color.reverb_blue_1));
        }
        currentShape.getShapeDrawable().setBounds(0, 0, 0, 0);
    }

    @Override
    protected void onTouchMove()
    {
        prepareRectDrawing();
    }

    @Override
    protected void onTouchUp()
    {
        shapeStack.add(currentShape);
        currentShape = new RectangleShape();
        currentShape.getShapeDrawable().getPaint().setColor(getResources().getColor(R.color.reverb_blue_1));
    }

    private void prepareRectDrawing() {

        int x1 =  (int)Math.min(touchDownPointX,latestTouchPointX);
        int x2 =  (int)Math.max(touchDownPointX, latestTouchPointX);
        int y1 =  (int)Math.min(touchDownPointY, latestTouchPointY);
        int y2 =  (int)Math.max(touchDownPointY, latestTouchPointY);
        this.currentShape.getShapeDrawable().setBounds(x1, y1, x2, y2);

        invalidate();
    }

}
