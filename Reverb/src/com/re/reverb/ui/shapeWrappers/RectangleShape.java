package com.re.reverb.ui.shapeWrappers;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.regions.CircleRegionShape;
import com.re.reverb.androidBackend.regions.RectangleRegionShape;
import com.re.reverb.androidBackend.regions.RegionShape;
import com.re.reverb.androidBackend.utils.GeographyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bill on 2014-11-27.
 */
public class RectangleShape extends Shape
{
    public RectangleShape(){
        this.shape = new ShapeDrawable(new android.graphics.drawable.shapes.RectShape());
    }

    @Override
    public RegionShape getReverbRegionShape(GoogleMap map)
    {
        int centreX = this.shape.getBounds().centerX();
        int centreY = this.shape.getBounds().centerY();
        int x1 = centreX - this.shape.getBounds().width()/2;
        int x2 = centreX + this.shape.getBounds().width()/2;
        int y1 = centreY - this.shape.getBounds().height()/2;
        int y2 = centreY + this.shape.getBounds().height()/2;
        LatLng p1LatLng = map.getProjection().fromScreenLocation(new Point(x1, y1));
        LatLng p2LatLng = map.getProjection().fromScreenLocation(new Point(x2, y1));
        LatLng p3LatLng = map.getProjection().fromScreenLocation(new Point(x1, y2));
        LatLng p4LatLng = map.getProjection().fromScreenLocation(new Point(x2, y2));
        Location p1Location = new Location(p1LatLng.latitude, p1LatLng.longitude);
        Location p2Location = new Location(p3LatLng.latitude, p3LatLng.longitude);
        Location p3Location = new Location(p4LatLng.latitude, p4LatLng.longitude);
        Location p4Location = new Location(p2LatLng.latitude, p2LatLng.longitude);

        return new RectangleRegionShape(p1Location, p2Location, p3Location, p4Location);
    }

    @Override
    public void drawOnMap(GoogleMap map)
    {
        RectangleRegionShape regionShape = (RectangleRegionShape) getReverbRegionShape(map);

        List<Location> rectPoints = regionShape.getPoints();
        List<LatLng> rectPointsLatLng = new ArrayList<LatLng>();
        for(Location l: rectPoints) {
            rectPointsLatLng.add(new LatLng(l.getLatitude(), l.getLongitude()));
        }
        Polygon rect = map.addPolygon(new PolygonOptions()
                .addAll(rectPointsLatLng)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));

    }
}