package com.re.reverb.androidBackend.regions.dto;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.regions.CircleRegionShape;

import java.util.ArrayList;

/**
 * Created by Bill on 2015-02-17.
 */
public class RegionCircleDto
{

    public double CentreLat;
    public double CentreLong;
    public double Radius;

    public RegionCircleDto(CircleRegionShape regionShape)
    {
        this.CentreLat = regionShape.getCentre().getLatitude();
        this.CentreLong = regionShape.getCentre().getLongitude();
        this.Radius = regionShape.getRadius();
    }
}
