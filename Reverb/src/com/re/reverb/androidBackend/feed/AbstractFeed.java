package com.re.reverb.androidBackend.feed;

import com.re.reverb.androidBackend.OnFeedDataChangedListener;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.androidBackend.post.ParentPost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public abstract class AbstractFeed implements Feed<ParentPost>
{
    protected List<OnFeedDataChangedListener> onDataChangedListeners = new ArrayList<OnFeedDataChangedListener>();
    protected ArrayList<ParentPost> posts = new ArrayList<ParentPost>();
    private Date latestPostTime;
    private Date earliestPostTime;

    @Override
    public void init() throws UnsuccessfulRefreshException
    {

    }

    @Override
    public void clearPosts()
    {
        this.posts.clear();
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

    @Override
    public void setOnDataChangedListener(OnFeedDataChangedListener listener)
    {
        this.onDataChangedListeners.add(listener);
    }

    public void notifyListenersOfDataChange()
    {
        for(OnFeedDataChangedListener l : onDataChangedListeners) {
            l.onDataChanged();
        }
    }
}
