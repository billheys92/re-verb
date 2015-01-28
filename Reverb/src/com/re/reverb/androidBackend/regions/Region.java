package com.re.reverb.androidBackend.regions;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.utils.SuccessStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Region
{

    protected boolean readPermission = true;
    protected boolean writePermission = true;

    protected int regionId;
    protected final double MAX_AREA = 10;    //10 km max area maybe?

    protected List<RegionShape> shapes = new ArrayList<RegionShape>();
    protected String name;
    protected String description;

    public Region(){
    }

    public Region(Region r){
        this.name = r.getName();
        this.description = r.getDescription();
        this.shapes = new ArrayList<RegionShape>(r.getShapes().size());
        for(RegionShape rs: r.getShapes()){
            this.shapes.add(rs);
        }
    }

    protected SuccessStatus removeAllShapes() {
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

    public List<RegionShape> getShapes() {
        return shapes;
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
        //TODO in here we should check for whether the user is inside and subscribed to the region
        return writePermission;
    }

    public void setWritePermission(boolean writePermission) {
        //TODO this function probably shouldn't exist, this should probably be handled in the getWritePermission function
        this.writePermission = writePermission;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    /*************************************************************
     * Editing regions code
     *
     * To edit a region:
     *  first call beginEditing to allow changes to be made.
     *  make changes by calling setters
     *  call saveRegion to save the region
     *      OR
     *  call discardChanges to stop editing and keep old changes
     *
     *
     *************************************************************/

    private boolean editing = false;
    private Region regionCopy;

    public boolean editing(){
        return editing;
    }

    public SuccessStatus canEdit(){
        if(this.writePermission) {
            return new SuccessStatus(true, "This region can be edited");
        }
        else {
            return new SuccessStatus(false, "You don't have permission to edit this region");
        }
    }

    public SuccessStatus beginEditing(){
        SuccessStatus canEdit = canEdit();
        if(canEdit.success())
        {
            if(!editing)
            {
                this.editing = true;
                this.regionCopy = new Region(this);
                return new SuccessStatus(true, "Began editing region");
            }
            else
            {
                return new SuccessStatus(true, "Already editing region");
            }
        }
        else
        {
            this.regionCopy = null;
            return new SuccessStatus(false,canEdit.reason());
        }
    }

    public SuccessStatus saveRegion() {
        if(this.editing) {
            SuccessStatus validation = validateRegion();
            if(validation.success()) {
                Reverb.getInstance().getRegionManager().addRegion(this);
                this.editing = false;
                this.regionCopy = null;
            }
            return validation;
        }
        return this.canEdit();
    }

    public void discardChanges(){
        this.name = regionCopy.getName();
        this.description = regionCopy.getDescription();
        Collections.copy(this.shapes, regionCopy.getShapes());
        this.editing = false;
        this.regionCopy = null;
    }

    public SuccessStatus validateRegion() {
        ArrayList<String> missingFields = new ArrayList<String>();
        if(this.getName() == null || this.getName().isEmpty()) {
            missingFields.add("You must provide a name for the region!");
        }
        if(this.getDescription() == null || this.getDescription().isEmpty()) {
            missingFields.add("You must provide a description for the region!");
        }
        if(this.getShapes() == null || this.getShapes().size() == 0) {
            missingFields.add("You must add some shapes to this region");
        }
        //TODO: also check for max area or other issues

        if(missingFields.size() > 0) {
            String message = "Some problems have to be fixed before you can save the region.\n";
            for(String s: missingFields){
                message += " - " + s + "\n";
            }
            return new SuccessStatus(false,message);
        }
        else {
            return new SuccessStatus(true,"Region can be saved");
        }
    }

    /**
     * Only works if called after beginEditing and before saveRegion or discardChanges
     * @param shapes
     */
    public void setShapes(List<RegionShape> shapes) {
        if(this.editing)
        {
            this.shapes = shapes;
        }
    }

    /**
     * Only works if called after beginEditing and before saveRegion or discardChanges
     *
     */
    public void setName(String name)
    {
        if(this.editing)
        {
            this.name = name;
        }
    }
    /**
     * Only works if called after beginEditing and before saveRegion or discardChanges
     */
    public void setDescription(String description)
    {
        if(this.editing)
        {
            this.description = description;
        }
    }


}
