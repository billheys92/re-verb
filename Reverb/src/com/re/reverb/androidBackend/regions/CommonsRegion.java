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

    private double regionRadius = 500; //in m

    public CommonsRegion(Location centre) {
        this.addShape(new CircleRegionShape(centre, regionRadius)).throwErrorIfFailed();
        this.writePermission = true;
        this.readPermission = true;
        this.subscribedTo = true;
        this.name = "Commons";
        this.description = "Post to others in your area!";
    }

    public SuccessStatus addShape(RegionShape shape){
        if (shape != null) {
            shapes.add(shape);
            return new SuccessStatus(true);
        } else {
            return new SuccessStatus(false, "There was no shape to add to the region");
        }
    }

    @Override
    public void setReadPermission(boolean val){

    }

    @Override
    public void setWritePermission(boolean val){

    }

    @Override
    public void subscribe(){

    }

    @Override
    public void unsubscribe(){

    }

    @Override
    public void update(){
        this.removeAllShapes().throwErrorIfFailed();
        this.addShape(new CircleRegionShape(Reverb.getInstance().getCurrentLocation(), regionRadius)).throwErrorIfFailed();
        super.update();
    }

    @Override
    public SuccessStatus canEdit(){
        return new SuccessStatus(false,"You can't edit the commons region");
    }

}
