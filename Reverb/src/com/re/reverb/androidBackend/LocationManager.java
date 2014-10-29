package com.re.reverb.androidBackend;

/**
 * Created by Bill on 2014-10-26.
 */
public class LocationManager {

    float lat;
    float longi;

    private static LocationManager ourInstance = new LocationManager();

    public static LocationManager getInstance() {
        return ourInstance;
    }

    private LocationManager() {
        lat = 0.0f;
        longi = 0.0f;
    };

    public void setLocation(float lat, float longi){
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
