package com.re.reverb.androidBackend.regions;

import com.re.reverb.R;
import com.re.reverb.androidBackend.Location;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Bill on 2015-01-29.
 */
public class RegionImageUrlFactory
{
    public static String createFromRegion(Region r){
        DecimalFormat df = new DecimalFormat("###.####");
        String urlBase = "http://maps.googleapis.com/maps/api/staticmap?";
        String centreLat = ""+r.getCentre().getLatitude();
        String centreLong = ""+r.getCentre().getLongitude();
        urlBase += "center=" + centreLat + "," + centreLong;
        urlBase  += r.getShapes().size() <= 3 ? "&zoom=15" : "&zoom=10";
        urlBase += "&size=600x600";
        urlBase += "&maptype=road";
        urlBase += "&sensor=false";
        for (RegionShape shape : r.getShapes())
        {
            ArrayList<Location> points = shape.getShapeAsPoints();
            urlBase += "&path=color%3ablack|weight:1|fillcolor%3ablack";
            for (Location location : points)
            {
                urlBase += "|" + df.format(location.getLatitude()) + "," + df.format(location.getLongitude());
            }
        }
        if(r.getShapes().size() > 0 && r.getShapes().size() <= 3)
        {
            urlBase += "&visible=";
            for (RegionShape shape : r.getShapes())
            {
                urlBase += shape.getCentre().getLatitude() + "," + shape.getCentre().getLongitude() + "|";
            }
        }
        String url = urlBase;
        return url;
    }
}
