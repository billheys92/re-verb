package com.re.reverb.androidBackend.regions;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.utils.SuccessStatus;

import java.util.ArrayList;

/**
 * Created by Bill on 2014-11-15.
 *
 * class invariant: always has one and only one circle region shape
 */
public class CommonsRegion extends Region{

    private double regionRadius = 3;    //3km

    public CommonsRegion(Location location) {
        super.addShape(new CircleRegionShape(location, regionRadius));
        this.writePermission = true;
        this.readPermission = true;
    }

    @Override
    public SuccessStatus addShape(RegionShape shape){
        return new SuccessStatus(false, "You can't add to the commons region");
    }

    @Override
    public void setReadPermission(boolean val){

    }

    @Override
    public void setWritePermission(boolean val){

    }

    @Override
    public void update(){
        super.removeAllShapes().throwErrorIfFailed();
        super.addShape(new CircleRegionShape(Reverb.getInstance().getCurrentLocation(), regionRadius)).throwErrorIfFailed();
        ArrayList<RegionShape> shapes = (ArrayList<RegionShape>)getShapes();
        RegionShape shape = shapes.get(0);
        boolean b = shape.containsPoint(new Location(47.5879637,-52.7008522));
        b = shape.containsPoint(new Location(47.396386,-52.9267519));
        super.update();
    }

}
