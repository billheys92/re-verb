package com.re.reverb.androidBackend;

public class Location
{
    float lat;
    float longi;
    Location( float lat, float longi ){
        this.lat = lat;
        this.longi = longi;
    }
	public float getLatitude()
    {
        return lat;
    }
    public float getLongitude()
    {
        return longi;
    }

}
