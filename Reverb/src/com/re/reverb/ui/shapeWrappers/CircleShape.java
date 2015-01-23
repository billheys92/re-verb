package com.re.reverb.ui.shapeWrappers;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.regions.CircleRegionShape;
import com.re.reverb.androidBackend.regions.RegionShape;
import com.re.reverb.androidBackend.utils.GeographyUtils;

/**
 * Created by Bill on 2014-11-27.
 */
public class CircleShape extends Shape
{

    public CircleShape(){
        this.shape = new ShapeDrawable(new android.graphics.drawable.shapes.OvalShape());
    }

    @Override
    public RegionShape getReverbRegionShape(GoogleMap map)
    {
        int centreX = this.shape.getBounds().centerX();
        int centreY = this.shape.getBounds().centerY();
        int shapeRadius = this.shape.getBounds().width()/2;
        int rightPoint = centreX+shapeRadius;
        LatLng rightLatLng = map.getProjection().fromScreenLocation(new Point(rightPoint, centreY));
        LatLng centre = map.getProjection().fromScreenLocation(new Point(centreX, centreY));

        Location centreLocation = new Location(centre.latitude, centre.longitude);
        Location rightLocation = new Location(rightLatLng.latitude, rightLatLng.longitude);

        double radius = GeographyUtils.distanceBetween(centreLocation,rightLocation);

        return new CircleRegionShape(centreLocation, radius);
    }

    @Override
    public void drawOnMap(GoogleMap map)
    {
        CircleRegionShape regionShape = (CircleRegionShape) getReverbRegionShape(map);

        LatLng centre = new LatLng(regionShape.getCentrePoint().getLatitude(),regionShape.getCentrePoint().getLongitude());
        Circle circle = map.addCircle(new CircleOptions()
                .center(centre)
                .radius(regionShape.getRadius())
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));

    }
}
