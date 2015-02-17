package com.re.reverb.androidBackend.regions;

import com.re.reverb.R;
import com.re.reverb.androidBackend.Location;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Bill on 2015-01-29.
 */
public class RegionImageUrlFactory
{
    public static URL createFromRegion(Region r){
        String urlBase = "http://maps.googleapis.com/maps/api/staticmap?";
        String centreLat = ""+r.getCentre().getLatitude();
        String centreLong = ""+r.getCentre().getLongitude();
        urlBase += "center=" + centreLat + "," + centreLong;
        urlBase  += "&zoom=15";
        urlBase += "&size=600x600";
        urlBase += "&maptype=road";
        urlBase += "&sensor=false";

        for(RegionShape shape: r.getShapes()) {
            ArrayList<Location> points = shape.getShapeAsPoints();
            urlBase += "&path=color%3ablack|weight:1|fillcolor%3ablack";
            for(Location location: points) {
                urlBase += "|" + location.getLatitude() + "," + location.getLongitude();
            }
        }
        if(r.getShapes().size() > 0)
        {
            urlBase += "&visible=";
            for (RegionShape shape : r.getShapes())
            {
                urlBase += shape.getCentre().getLatitude() + "," + shape.getCentre().getLongitude() + "|";
            }
        }
        String url = urlBase;
        try
        {
            return new URL(url);
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
