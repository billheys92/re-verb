package com.re.reverb.androidBackend.regions;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.utils.SuccessStatus;

import java.util.ArrayList;
import java.util.Collection;

public class Region
{

    protected boolean readPermission = true;
    protected boolean writePermission = true;

    protected int regionId;
    protected final double MAX_AREA = 10;    //10 km max area maybe?

    private Collection<RegionShape> shapes = new ArrayList<RegionShape>();

    /**
     *
     * @param shape
     * @return true if the operation was successful
     */
    public SuccessStatus addShape(RegionShape shape) {
        if (writePermission) {
            if (shape != null) {
                if(getArea() < MAX_AREA) {
                    shapes.add(shape);
                    return new SuccessStatus(true);
                } else {
                    return new SuccessStatus(false,"Max region area has been exceeded");
                }
            } else {
                return new SuccessStatus(false, "There was no shape to add to the region");
            }
        } else {
            return new SuccessStatus(false, "You don't have permission to add to this region");
        }
    }

    public SuccessStatus removeAllShapes() {
        if(writePermission) {
            this.shapes.clear();
            return new SuccessStatus(true);
        } else {
            return new SuccessStatus(false, "You don't have permission to clear this region");
        }
    }

    public double getArea(){
        double area = 0.0;
        for(RegionShape shape: shapes) {
            area += shape.getArea();
        }
        return area;
    }

    public boolean containsPoint(Location point){
        boolean contains = false;
        for(RegionShape shape: shapes) {
            if(shape.containsPoint(point)) contains = true;
        }
        return contains;
    }

    public void update() {

    }

    public Collection<RegionShape> getShapes() {
        return shapes;
    }

    public void setShapes(Collection<RegionShape> shapes) {
        this.shapes = shapes;
    }

    public int getRegionId(){
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public boolean getReadPermission() {
        return readPermission;
    }

    public void setReadPermission(boolean readPermission) {
        this.readPermission = readPermission;
    }

    public boolean getWritePermission() {
        return writePermission;
    }

    public void setWritePermission(boolean writePermission) {
        this.writePermission = writePermission;
    }

}
