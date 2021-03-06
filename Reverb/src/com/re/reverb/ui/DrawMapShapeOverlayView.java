package com.re.reverb.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.re.reverb.R;
import com.re.reverb.ui.shapeWrappers.CircleShape;
import com.re.reverb.ui.shapeWrappers.Shape;

import java.util.Stack;

/**
 * Created by Bill on 2015-01-22.
 */
public abstract class DrawMapShapeOverlayView extends View
{
    protected Shape currentShape;
    float touchDownPointX = 0.0f;
    float touchDownPointY = 0.0f;
    float latestTouchPointX = 0.0f;
    float latestTouchPointY = 0.0f;
    protected ShapeAddedToOverlayListener shapeAddedListener;

    public DrawMapShapeOverlayView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

    }

    public DrawMapShapeOverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawMapShapeOverlayView(Context context) {
        this(context, null, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchDownPointX = event.getX();
                touchDownPointY = event.getY();
                Log.d("Reverb", "DOWN -- " + touchDownPointX + ", " + touchDownPointY);
                onTouchDown();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                latestTouchPointX = event.getX();
                latestTouchPointY = event.getY();
                Log.d("Reverb", "TOUCH -- " + latestTouchPointX + ", " + latestTouchPointY);
                onTouchMove();
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp();
                break;
            default:
                super.onTouchEvent(event);
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        for(Shape shape: shapeStack) {
//            shape.getShapeDrawable().draw(canvas);
//        }

        if(currentShape != null)
        {
            currentShape.getShapeDrawable().draw(canvas);
        }
    }

    public void attachShapeAddedToOverlayListener(ShapeAddedToOverlayListener listener) {
        this.shapeAddedListener = listener;
    }

    protected abstract void onTouchDown();
    protected abstract void onTouchMove();
    protected abstract void onTouchUp();
}
