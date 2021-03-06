package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.account.UserProfile;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.feed.Feed;
import com.re.reverb.androidBackend.post.dto.CreatePostDto;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.androidBackend.regions.RegionChangeListener;
import com.re.reverb.network.AWSPersistenceManager;
import com.re.reverb.network.PostManagerImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class Reverb {

    private static Reverb ourInstance = new Reverb();

    public static Reverb getInstance() {
        return ourInstance;
    }

    //listeners
    private static Collection<LocationUpdateListener> locationUpdateListeners;
    private static Collection<AvailableRegionsUpdateRegion> availableRegionsUpdateListeners;
    private static Collection<RegionChangeListener> regionChangeListeners;

    //private UserProfile currentUser;
    private UserProfile currentUser;
    private RegionManager regionManager;
    private Settings settings = Settings.getInstance();
    public LocationManager locationManager;
    private boolean anonymous = false;

    private Reverb()
    {
        locationUpdateListeners = new ArrayList<LocationUpdateListener>();
        availableRegionsUpdateListeners = new ArrayList<AvailableRegionsUpdateRegion>();
        regionChangeListeners = new ArrayList<RegionChangeListener>();
        locationManager = new LocationManager();
        this.regionManager = new RegionManagerImpl();

    }

    public void signInUser(UserProfile user)
    {
        this.currentUser = user;
    }

    public void submitPost(CreatePostDto postDto)
    {
        PostManagerImpl.submitPost(postDto);
    }

    public void submitPost(CreatePostDto postDto, File image)
    {
        PostManagerImpl.submitPost(postDto, image);
    }

    public RegionManager getRegionManager()
    {
        return regionManager;
    }

    public int getCurrentUserId() throws NotSignedInException{
        if(currentUser == null){
            throw new NotSignedInException("Get User ID");
        }
        return currentUser.User_id;
    }

    public UserProfile getCurrentUser()  throws NotSignedInException{
        if(currentUser == null){
            throw new NotSignedInException("Get User ID");
        }
        return currentUser;
    }

    public boolean isAnonymous()
    {
        return anonymous;
    }

    public void toggleAnonymity()
    {
        anonymous = !anonymous;
    }

    public Settings getSettings() {
        return settings;
    }

    public Location getCurrentLocation() {
        return this.locationManager.getCurrentLocation();
    }

    public void setCurrentLocation(float lat, float longi) {
        this.locationManager.setCurrentLocation(lat, longi);
        notifyLocationListeners();
    }

    public static void attachLocationListener(LocationUpdateListener listener) {
        locationUpdateListeners.add(listener);
    }

    private void notifyLocationListeners(){
        for(LocationUpdateListener l: this.locationUpdateListeners){
            l.onLocationChanged(this.locationManager.getCurrentLocation());
        }
    }
    public static void attachAvailableRegionsUpdateListener(AvailableRegionsUpdateRegion listener) {
        availableRegionsUpdateListeners.add(listener);
    }

    public static void notifyAvailableRegionsUpdateListeners(){
        for(AvailableRegionsUpdateRegion l: availableRegionsUpdateListeners){
            l.onAvailableRegionsUpdated();
        }
    }

    public static void attachRegionChangedListener(RegionChangeListener listener) {
        regionChangeListeners.add(listener);
    }

    public static void notifyRegionChangedListeners(){
        Region newRegion = getInstance().getRegionManager().getCurrentRegion();
        for(RegionChangeListener l: regionChangeListeners){
            l.onRegionChanged(newRegion);
        }
    }



}

