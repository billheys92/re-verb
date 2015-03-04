package com.re.reverb.androidBackend.regions.dto;

import com.re.reverb.androidBackend.Location;

/**
 * Created by Bill on 2015-02-21.
 */
public class GetNearbyRegionsDto
{
    private double Location_lat;
    private double Location_long;
    public GetNearbyRegionsDto(Location currentLocation)
    {
        this.Location_lat = currentLocation.getLatitude();
        this.Location_long = currentLocation.getLongitude();
    }

    public double getLat()
    {
        return Location_lat;
    }

    public double getLong()
    {
        return Location_long;
    }
}
