package com.re.reverb.ui.shapeWrappers;

import android.graphics.drawable.ShapeDrawable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.re.reverb.androidBackend.regions.RegionShape;

/**
 * Created by Bill on 2014-11-27.
 */
public abstract class Shape
{
    protected ShapeDrawable shape;
    public ShapeDrawable getShapeDrawable(){
        return shape;
    }
    protected abstract RegionShape getReverbRegionShape(GoogleMap map);
    public abstract void drawOnMap(GoogleMap map);
}
