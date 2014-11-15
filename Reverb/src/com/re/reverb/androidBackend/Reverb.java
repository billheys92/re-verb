package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.regions.CommonsRegion;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.network.AWSPersistenceManager;

public class Reverb {

    private static Reverb ourInstance = new Reverb();

    public static Reverb getInstance() {
        return ourInstance;
    }


    //private UserProfile currentUser;
    private UserProfile currentUser = new UserProfile("username@domain.com","Bill Heys","@billheys","re:verb developer",0);
    private Region currentRegion;
    private Feed postFeed;
    private Settings settings = Settings.getInstance();
    public LocationManager locationManager;

    private Reverb(){

        locationManager = new LocationManager();

    }

    public void signInUser(UserProfile user)
    {
        this.currentUser = user;
    }

    public boolean submitPost(Post post)
    {
        //send post to PersistenceManager
        AWSPersistenceManager testPer = new AWSPersistenceManager();
        return testPer.submitPost(post);
//This should be commented back in but isn't working yet
//        try {
//            postFeed.getPosts().add(0,post);
//        } catch (UnsuccessfulRefreshException e) {
//            e.printStackTrace();
//        }
        //return false;
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

    public Location getCurrentLocation() {
        return this.locationManager.getCurrentLocation();
    }

    public void setCurrentLocation(float lat, float longi) {
        this.locationManager.setCurrentLocation(lat, longi);

        if (currentRegion == null) {
            currentRegion = new CommonsRegion();
        }
        currentRegion.update();
    }

    public Settings getSettings() {
        return settings;
    }

}

