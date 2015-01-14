package com.re.reverb.androidBackend.regions;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.utils.GeographyUtils;

/**
 * Created by Bill on 2014-11-15.
 */
public class CircleRegionShape extends RegionShape {

    private Location centrePoint;
    private double radius;

    public CircleRegionShape(Location centrePoint, double radius) {
        this.centrePoint = centrePoint;
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.PI*Math.pow(this.radius,2);
    }

    @Override
    public boolean containsPoint(Location point) {
        double distanceFromCentre = GeographyUtils.distanceBetween(point,centrePoint);
        return distanceFromCentre < radius;
    }
}
