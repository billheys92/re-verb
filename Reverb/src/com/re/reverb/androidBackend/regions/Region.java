package com.re.reverb.androidBackend.regions;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.utils.GeographyUtils;
import com.re.reverb.androidBackend.utils.SuccessStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Region
{

    protected boolean readPermission = true;
    protected boolean writePermission = true;

    protected boolean canEdit = true;
    protected boolean subscribedTo = false;

    Random r = new Random();
    protected int regionId;
    protected final double MAX_AREA = 10;    //10 km max area maybe?

    protected List<RegionShape> shapes = new ArrayList<RegionShape>();
    protected String name;
    protected String description;
    protected Date creationTime;
    protected Date updateTime;
    protected File thumbnail;
    protected String thumbnailUrl;
    protected int numMembers;
    protected int numPosts;

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

    public Location getCentre(){
        List<Location> shapeCentres = new ArrayList<Location>();
        for(RegionShape shape: shapes) {
            shapeCentres.add(shape.getCentre());
        }
        return GeographyUtils.centreOfLocations(shapeCentres);
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

    public Date getCreationTime()
    {
        return creationTime;
    }

    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setShapes(ArrayList<RegionShape> shapes)
    {
        this.shapes = shapes;
    }

    public void subscribe()
    {
        this.subscribedTo = true;
    }

    public int getNumMembers()
    {
        return numMembers;
    }

    public void setNumMembers(int numMembers)
    {
        this.numMembers = numMembers;
    }

    public int getNumPosts()
    {
        return numPosts;
    }

    public void setNumPosts(int numPosts)
    {
        this.numPosts = numPosts;
    }


    public void unsubscribe()
    {
        this.subscribedTo = false;
    }

    public boolean canUnsubscribe(){
        return true;
    }

    public boolean canSubscribe()
    {
        return !Reverb.getInstance().getRegionManager().isRegionSubscribed(this.regionId);
    }

    public boolean isSubscribedTo() {
        return this.subscribedTo;
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
        if(this.canEdit) {
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
                Reverb.getInstance().getRegionManager().createNewRegion(this);
                this.editing = false;
                this.canEdit = false;
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
    public void editShapes(List<RegionShape> shapes) {
        if(this.editing)
        {
            this.shapes = shapes;
        }
    }

    /**
     * Only works if called after beginEditing and before saveRegion or discardChanges
     *
     */
    public void editName(String name)
    {
        if(this.editing)
        {
            this.name = name;
        }
    }
    /**
     * Only works if called after beginEditing and before saveRegion or discardChanges
     */
    public void editDescription(String description)
    {
        if(this.editing)
        {
            this.description = description;
        }
    }

    public void setThumbnail(File thumbnailFile)
    {
        this.thumbnail = thumbnailFile;
    }

    public File getThumbnail()
    {
        return thumbnail;
    }


    public String getThumbnailUrl()
    {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl)
    {
        this.thumbnailUrl = thumbnailUrl;
    }

}
