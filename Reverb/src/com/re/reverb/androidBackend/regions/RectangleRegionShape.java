package com.re.reverb.androidBackend.regions;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.utils.GeographyUtils;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

/**
 * Created by Bill on 2015-01-23.
 */
public class RectangleRegionShape extends RegionShape
{

    private List<Location> points = new ArrayList<Location>();

    /**
     * @pre p1-p4 must be sorted in clockwise order starting at the top-left
     * @param p1
     * @param p2
     * @param p3
     * @param p4
     */
    public RectangleRegionShape(Location p1, Location p2, Location p3, Location p4){
        this.points.add(p1);
        this.points.add(p2);
        this.points.add(p3);
        this.points.add(p4);
    }

    public List<Location> getPoints(){
        return points;
    }

    @Override
    public double getArea()
    {
        //TODO: points should be sorted first. If points aren't in clockwise or counter-clockwise order this won't work
        double width = GeographyUtils.distanceBetween(points.get(0), points.get(1));
        double height = GeographyUtils.distanceBetween(points.get(3), points.get(0));
        return width*height;
    }

    @Override
    public boolean containsPoint(Location point)
    {
        //TODO: do this
        return false;
    }

}
