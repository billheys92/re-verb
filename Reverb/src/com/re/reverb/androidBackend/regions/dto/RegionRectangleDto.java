package com.re.reverb.androidBackend.regions.dto;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.regions.RectangleRegionShape;

import java.util.ArrayList;

/**
 * Created by Bill on 2015-02-17.
 */
public class RegionRectangleDto
{
    public double p1Lat;
    public double p1Long;
    public double p2Lat;
    public double p2Long;
    public double p3Lat;
    public double p3Long;
    public double p4Lat;
    public double p4Long;

    public RegionRectangleDto(RectangleRegionShape rectangle)
    {
        ArrayList<Location> rectPoints = (ArrayList<Location>)rectangle.getPoints();
        this.p1Lat = rectPoints.get(0).getLatitude();
        this.p1Long = rectPoints.get(0).getLongitude();
        this.p2Lat = rectPoints.get(1).getLatitude();
        this.p2Long = rectPoints.get(1).getLongitude();
        this.p3Lat = rectPoints.get(2).getLatitude();
        this.p3Long = rectPoints.get(2).getLongitude();
        this.p4Lat = rectPoints.get(3).getLatitude();
        this.p4Long = rectPoints.get(3).getLongitude();
    }
}
