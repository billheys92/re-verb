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

public class NewPostFeed extends AbstractFeed implements LocationUpdateListener
{

    private Context context;

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
    public void refreshPosts() throws UnsuccessfulRefreshException {
        if(Reverb.getInstance().getRegionManager().getCurrentRegion().getRegionId() == 0)
        {
            Location location = Reverb.getInstance().getCurrentLocation();
            PostManagerImpl.getRefreshPosts(location.getLatitude(), location.getLongitude(), 2, this);
        }
        else
        {
            PostManagerImpl.getPostsForRegion(Reverb.getInstance().getRegionManager().getCurrentRegion().getRegionId(), this);
        }
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
