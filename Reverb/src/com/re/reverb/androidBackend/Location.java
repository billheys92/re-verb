package com.re.reverb.androidBackend;

public class Location
{
    double lat;
    double longi;
    public Location( double lat, double longi ){
        this.lat = lat;
        this.longi = longi;
    }
	public double getLatitude()
    {
        return lat;
    }
    public double getLongitude()
    {
        return longi;
    }

}
