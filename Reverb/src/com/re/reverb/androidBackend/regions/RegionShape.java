package com.re.reverb.androidBackend.regions;

import com.re.reverb.androidBackend.Location;

import java.util.ArrayList;

/**
 * Created by Bill on 2014-11-15.
 */
public abstract class RegionShape {

    public abstract double getArea();
    public abstract boolean containsPoint(Location point);
    public abstract Location getCentre();
    public abstract ArrayList<Location> getShapeAsPoints();
}
