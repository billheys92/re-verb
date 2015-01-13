package com.re.reverb.ui.shapeWrappers;

import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;

import com.google.android.gms.maps.GoogleMap;
import com.re.reverb.androidBackend.regions.RegionShape;

/**
 * Created by Bill on 2014-11-27.
 */
public class RectangleShape extends Shape
{
    public RectangleShape(){
        this.shape = new ShapeDrawable(new android.graphics.drawable.shapes.RectShape());
    }

    @Override
    public RegionShape getReverbRegionShape(GoogleMap map)
    {
        return null;
    }
}