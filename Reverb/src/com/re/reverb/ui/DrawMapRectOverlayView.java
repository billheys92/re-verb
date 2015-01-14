package com.re.reverb.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.re.reverb.R;
import com.re.reverb.ui.shapeWrappers.RectangleShape;
import com.re.reverb.ui.shapeWrappers.Shape;

import java.util.Stack;

public class DrawMapRectOverlayView extends View {

    Shape currentRectDrawable;
    Stack<Shape> rectStack = new Stack<Shape>();

    float touchDownPointX = 0.0f;
    float touchDownPointY = 0.0f;
    float latestTouchPointX = 0.0f;
    float latestTouchPointY = 0.0f;

    public DrawMapRectOverlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        currentRectDrawable = new RectangleShape();
        currentRectDrawable.getShapeDrawable().getPaint().setColor(getResources().getColor(R.color.reverb_blue_1));
    }

    public DrawMapRectOverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawMapRectOverlayView(Context context) {
        this(context, null, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchDownPointX = event.getX();
                touchDownPointY = event.getY();
                Log.d("Reverb","DOWN -- "+touchDownPointX+", "+touchDownPointY);
                currentRectDrawable.getShapeDrawable().setBounds(0, 0, 0, 0);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                latestTouchPointX = event.getX();
                latestTouchPointY = event.getY();
                Log.d("Reverb","TOUCH -- "+latestTouchPointX+", "+latestTouchPointY);

                prepareDrawing();
                break;
            case MotionEvent.ACTION_UP:
                rectStack.add(currentRectDrawable);
                currentRectDrawable = new RectangleShape();
                currentRectDrawable.getShapeDrawable().getPaint().setColor(getResources().getColor(R.color.reverb_blue_1));
                break;
            default:
                super.onTouchEvent(event);
        }

        return true;
    }

    private void prepareDrawing() {

        int x1 =  (int)Math.min(touchDownPointX,latestTouchPointX);
        int x2 =  (int)Math.max(touchDownPointX, latestTouchPointX);
        int y1 =  (int)Math.min(touchDownPointY, latestTouchPointY);
        int y2 =  (int)Math.max(touchDownPointY, latestTouchPointY);
        currentRectDrawable.getShapeDrawable().setBounds(x1, y1, x2, y2);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(Shape rect: rectStack) {
            rect.getShapeDrawable().draw(canvas);
        }
        currentRectDrawable.getShapeDrawable().draw(canvas);
    }
}