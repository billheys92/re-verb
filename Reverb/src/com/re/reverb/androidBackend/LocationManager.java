package com.re.reverb.androidBackend;

/**
 * Created by Bill on 2014-10-26.
 */
public class LocationManager {

    float currentLat;
    float currentLongi;

    public LocationManager() {
        currentLat = 0.0f;
        currentLongi = 0.0f;
    };

    public void setCurrentLocation(float lat, float longi){
        this.currentLat = lat;
        this.currentLongi = longi;
    }

    public Location getCurrentLocation() {
        return new Location(currentLat, currentLongi);
    }
    public float getCurrentLatitude()
    {
        return currentLat;
    }
    public float getCurrentLongitude()
    {
        return currentLongi;
    }


}
