package com.re.reverb.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.re.reverb.R;
import com.re.reverb.ui.shapeWrappers.CircleShape;
import com.re.reverb.ui.shapeWrappers.Shape;

public class DrawMapCircleOverlayView extends DrawMapShapeOverlayView
{

    public DrawMapCircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.currentShape = new CircleShape();
        this.currentShape.getShapeDrawable().getPaint().setColor(getResources().getColor(R.color.map_shape_color));
    }

    public DrawMapCircleOverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawMapCircleOverlayView(Context context) {
        this(context, null, 0);
    }

    @Override
    protected void onTouchDown()
    {
        if(this.currentShape == null) {
            this.currentShape = new CircleShape();
            this.currentShape.getShapeDrawable().getPaint().setColor(getResources().getColor(R.color.map_shape_color));
        }
        currentShape.getShapeDrawable().setBounds(0, 0, 0, 0);
    }

    @Override
    protected void onTouchMove()
    {
        prepareCircleDrawing();
    }

    @Override
    protected void onTouchUp()
    {
        this.shapeAddedListener.shapeAdded(currentShape);
        resetCurrentShape();
    }

    private void resetCurrentShape(){
        currentShape = new CircleShape();
        currentShape.getShapeDrawable().getPaint().setColor(getResources().getColor(R.color.map_shape_color));
        this.touchDownPointX = 0;
        this.touchDownPointY = 0;
    }

    private void prepareCircleDrawing() {

        float sideLengthOver2 = Math.max(Math.abs(touchDownPointX - latestTouchPointX),Math.abs(touchDownPointY - latestTouchPointY));
        int x1 =  (int)(touchDownPointX - sideLengthOver2);
        int x2 =  (int)(touchDownPointX + sideLengthOver2);
        int y1 =  (int)(touchDownPointY - sideLengthOver2);
        int y2 =  (int)(touchDownPointY + sideLengthOver2);
        currentShape.getShapeDrawable().setBounds(x1, y1, x2, y2);

        invalidate();
    }


}