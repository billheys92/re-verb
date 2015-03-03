package com.re.reverb.androidBackend.regions;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.utils.GeographyUtils;

import java.util.ArrayList;

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

    @Override
    public Location getCentre()
    {
        return centrePoint;
    }

    @Override
    public ArrayList<Location> getShapeAsPoints()
    {
        int earthRad = 6371;
        int numPoints = 24;
        ArrayList<Location> pts = new ArrayList<Location>(360/numPoints);

        double latitude  = (centrePoint.getLatitude() * Math.PI) / 180;
        double longitude  = (centrePoint.getLongitude() * Math.PI) / 180;
        double d    = (radius/1000) / earthRad;

        for(int i = 0; i <= 360; i+=360/numPoints)
        {
            double rad = i * Math.PI / 180;
            double lat = Math.asin((Math.sin(latitude) * Math.cos(d) + Math.cos(latitude) * Math.sin(d) * Math.cos(rad)));
            double longi = ((longitude + Math.atan2(Math.sin(rad) * Math.sin(d) * Math.cos(latitude), Math.cos(d) - Math.sin(latitude) * Math.sin(lat))) * 180) / Math.PI;
            lat = (lat * 180) / Math.PI;
            pts.add(new Location(lat, longi));
        }
        return pts;
    }

    public double getRadius(){
        return radius;
    }

    public Location getCentrePoint(){
        return centrePoint;
    }
}
