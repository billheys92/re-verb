package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.feed.Feed;
import com.re.reverb.androidBackend.post.LazyPostBuilder;


public class Reverb {

    private static Reverb ourInstance = new Reverb();

    public static Reverb getInstance() {
        return ourInstance;
    }


    //private UserProfile currentUser;
    private UserProfile currentUser = new UserProfile("username@domain.com","Name","nickname","default description",0);
    private Region currentRegion;
    private Feed postFeed;
    private LazyPostBuilder postBuilder;
    private Settings settings = Settings.getInstance();

    public Reverb(){
        currentRegion = new CommonsRegion();
    }

    public void signInUser(UserProfile user)
    {
        this.currentUser = user;
        try {
            this.postBuilder = new LazyPostBuilder(this);
        } catch (NotSignedInException e) {
            e.printStackTrace();
        }
    }

    public boolean submitPost() throws NotSignedInException{
        if(postBuilder == null){
            throw new NotSignedInException("Submit post");
        }

        return false;
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

}

