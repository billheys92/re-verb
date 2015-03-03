package com.re.reverb.androidBackend.regions.dto;

/**
 * Created by Bill on 2015-03-03.
 */
public class ReceiveRegionCircleDto
{
    private double Radius;
    private double lat;
    private double lon;

    public ReceiveRegionCircleDto(double Radius,
                                  double lat,
                                  double lon)
    {
        this.Radius = Radius;
        this.lat =lat;
        this.lon = lon;
    }

    public double getRadius()
    {
        return Radius;
    }

    public double getLat()
    {
        return lat;
    }

    public double getLon()
    {
        return lon;
    }

}
