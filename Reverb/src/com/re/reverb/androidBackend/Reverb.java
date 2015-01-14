package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.feed.Feed;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.account.UserProfile;
import com.re.reverb.androidBackend.post.dto.CreatePostDto;
import com.re.reverb.androidBackend.regions.CommonsRegion;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.network.AWSPersistenceManager;

import java.util.ArrayList;
import java.util.List;

public class Reverb {

    private static Reverb ourInstance = new Reverb();

    public static Reverb getInstance() {
        return ourInstance;
    }


    //private UserProfile currentUser;
    private UserProfile currentUser;
    private Region currentRegion;
    private List<Region> availableRegions;
    private Feed postFeed;
    private Settings settings = Settings.getInstance();
    private final AWSPersistenceManager persistenceManager = new AWSPersistenceManager();
    public LocationManager locationManager;

    private Reverb()
    {
        currentUser = new UserProfile("username@domain.com","Bill Heys","@billheys","re:verb developer",0);
        locationManager = new LocationManager();
        availableRegions = new ArrayList<Region>();
        availableRegions.add(new CommonsRegion(locationManager.getCurrentLocation()));
        currentRegion = availableRegions.get(0);
    }

    public void signInUser(UserProfile user)
    {
        this.currentUser = user;
    }

    public boolean submitPost(CreatePostDto postDto)
    {
        return persistenceManager.getPostManager().submitPost(postDto);

        //send post to PersistenceManager
        //AWSPersistenceManager testPer = new AWSPersistenceManager();
        //return testPer.submitPost(postDto);
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
        currentRegion.update();
    }

    public Settings getSettings() {
        return settings;
    }

}

