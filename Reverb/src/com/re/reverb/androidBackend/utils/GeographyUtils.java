package com.re.reverb.androidBackend.utils;

import com.re.reverb.androidBackend.Location;

/**
 * Created by Bill on 2014-11-15.
 */
public class GeographyUtils {

    //return the distance between two points in metres
    public static double distanceBetween(Location loc1, Location loc2) {
        return distFrom(loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude());
    }

    //return the distance between two points in metres
    private static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371;  //in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist*1000;
    }
}
