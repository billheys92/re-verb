package com.re.reverb.androidBackend.feed;

import android.content.Context;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.LocationUpdateListener;
import com.re.reverb.androidBackend.OnFeedDataChangedListener;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.androidBackend.post.ParentPost;
import com.re.reverb.network.PostManagerImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NewPostFeed implements Feed<ParentPost>, LocationUpdateListener
{
    private List<OnFeedDataChangedListener> onDataChangedListeners = new ArrayList<OnFeedDataChangedListener>();
    protected ArrayList<ParentPost> posts = new ArrayList<ParentPost>();
    private Context context;
    private Date latestPostTime;
    private Date earliestPostTime;

    public NewPostFeed(Context context)
    {
        this.context = context;
    }

    @Override
    public void init() throws UnsuccessfulRefreshException
    {
        Reverb.getInstance().attachLocationListener(this);
    }

    @Override
    public ArrayList<ParentPost> getPosts()
    {
        return posts;
    }

    @Override
    public boolean setPosts(ArrayList<ParentPost> posts){
        if(posts != null && posts.size() != 0) {
            this.posts = posts;
            Collections.reverse(posts);
            notifyListenersOfDataChange();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void setLastPostTime(Date timestamp)
    {
        this.latestPostTime = timestamp;
    }

    @Override
    public String getLastPostTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        if(latestPostTime != null)
        {
            return sdf.format(latestPostTime);
        }
        else
        {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date now = new Date();
            return sdf.format(now);
        }
    }

    @Override
    public void setEarliestPostTime(Date timestamp)
    {
        this.earliestPostTime = timestamp;
    }

    @Override
    public String getEarliestPostTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
        if(earliestPostTime != null)
        {
            return sdf.format(earliestPostTime);
        }
        else
        {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date now = new Date();
            return sdf.format(now);
        }
    }


    public void notifyListenersOfDataChange()
    {
        for(OnFeedDataChangedListener l : onDataChangedListeners) {
            l.onDataChanged();
        }
    }

    @Override
    public void setOnDataChangedListener(OnFeedDataChangedListener listener)
    {
        this.onDataChangedListeners.add(listener);
    }

    @Override
    public void refreshPosts() throws UnsuccessfulRefreshException {
        //TODO: Add real range value
        Location location = Reverb.getInstance().getCurrentLocation();
        PostManagerImpl.getRefreshPosts(location.getLatitude(), location.getLongitude(), 2, this);
    }

    @Override
    public boolean fetchMore() throws Exception
    {
        Location location = Reverb.getInstance().getCurrentLocation();
        PostManagerImpl.getPosts(location.getLatitude(), location.getLongitude(), 2, this);
        return false;
    }

    @Override
    public void onLocationChanged(Location newLocation)
    {
        try
        {
            this.refreshPosts();
        } catch (UnsuccessfulRefreshException e)
        {

            e.printStackTrace();
        }
    }
}
