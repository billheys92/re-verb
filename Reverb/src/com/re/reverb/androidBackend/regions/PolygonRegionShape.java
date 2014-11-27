package com.re.reverb.androidBackend.regions;

import com.re.reverb.androidBackend.Location;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Bill on 2014-11-15.
 */
public class PolygonRegionShape extends RegionShape{

    private ArrayList<Location> vertices;

    public PolygonRegionShape(ArrayList<Location> vertices) {
        this.vertices = vertices;
    }

    @Override
    public double getArea() {
        return 0;
    }

    @Override
    public boolean containsPoint(Location point) {
        return false;
    }
}
