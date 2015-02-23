package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.feed.Feed;
import com.re.reverb.androidBackend.account.UserProfile;
import com.re.reverb.androidBackend.post.dto.CreatePostDto;
import com.re.reverb.network.AWSPersistenceManager;
import com.re.reverb.network.PostManagerImpl;

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

    //private UserProfile currentUser;
    private UserProfile currentUser = new UserProfile("test", "test", "test", "test", "test", 8);
    private RegionManager regionManager;
    private Feed postFeed;
    private Settings settings = Settings.getInstance();
    private final AWSPersistenceManager persistenceManager = new AWSPersistenceManager();
    public LocationManager locationManager;

    private Reverb()
    {
        locationUpdateListeners = new ArrayList<LocationUpdateListener>();
        availableRegionsUpdateListeners = new ArrayList<AvailableRegionsUpdateRegion>();
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

    public RegionManager getRegionManager()
    {
        return regionManager;
    }

    public int getCurrentUserId() throws NotSignedInException{
        if(currentUser == null){
            throw new NotSignedInException("Get User ID");
        }
        return currentUser.getUserId();
    }

    public UserProfile getCurrentUser()  throws NotSignedInException{
        if(currentUser == null){
            throw new NotSignedInException("Get User ID");
        }
        return currentUser;
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



}

