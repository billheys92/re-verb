package com.re.reverb.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.re.reverb.R;
import com.re.reverb.ui.shapeWrappers.CircleShape;
import com.re.reverb.ui.shapeWrappers.Shape;

import java.util.List;
import java.util.Stack;

public class DrawMapCircleOverlayView extends View {

    Shape currentCircleDrawable;
    Stack<Shape> circlesStack = new Stack<Shape>();

    float touchDownPointX = 0.0f;
    float touchDownPointY = 0.0f;
    float latestTouchPointX = 0.0f;
    float latestTouchPointY = 0.0f;

    public DrawMapCircleOverlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        currentCircleDrawable = new CircleShape();
        currentCircleDrawable.getShapeDrawable().getPaint().setColor(getResources().getColor(R.color.reverb_blue_1));
    }

    public DrawMapCircleOverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawMapCircleOverlayView(Context context) {
        this(context, null, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchDownPointX = event.getX();
                touchDownPointY = event.getY();
                Log.d("Reverb","DOWN -- "+touchDownPointX+", "+touchDownPointY);
                currentCircleDrawable.getShapeDrawable().setBounds(0, 0, 0, 0);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                latestTouchPointX = event.getX();
                latestTouchPointY = event.getY();
                Log.d("Reverb","TOUCH -- "+latestTouchPointX+", "+latestTouchPointY);

                prepareCircleDrawing();
                break;
            case MotionEvent.ACTION_UP:
                circlesStack.add(currentCircleDrawable);
                currentCircleDrawable = new CircleShape();
                currentCircleDrawable.getShapeDrawable().getPaint().setColor(getResources().getColor(R.color.reverb_blue_1));
                break;
            default:
                super.onTouchEvent(event);
        }

        return true;
    }

    private void prepareCircleDrawing() {

        float sideLengthOver2 = Math.max(Math.abs(touchDownPointX - latestTouchPointX),Math.abs(touchDownPointY - latestTouchPointY));
        int x1 =  (int)(touchDownPointX - sideLengthOver2);
        int x2 =  (int)(touchDownPointX + sideLengthOver2);
        int y1 =  (int)(touchDownPointY - sideLengthOver2);
        int y2 =  (int)(touchDownPointY + sideLengthOver2);
        currentCircleDrawable.getShapeDrawable().setBounds(x1, y1, x2, y2);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(Shape circle: circlesStack) {
            circle.getShapeDrawable().draw(canvas);
        }
        currentCircleDrawable.getShapeDrawable().draw(canvas);
    }
}