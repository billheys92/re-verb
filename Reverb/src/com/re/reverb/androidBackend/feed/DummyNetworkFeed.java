package com.re.reverb.androidBackend.feed;

import com.re.reverb.androidBackend.OnFeedDataChangedListener;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.network.AWSPersistenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DummyNetworkFeed implements Feed
{
    protected static final int FEED_SIZE = 10;
    protected int queuePosition = 0;
    private List<OnFeedDataChangedListener> onDataChangedListeners = new ArrayList<OnFeedDataChangedListener>();
    protected ArrayList<Post> posts = new ArrayList<Post>();

    @Override
    public void init() throws UnsuccessfulRefreshException
    {
        if(this.posts.size() == 0)
        {
            refreshPosts();
        }
    }

    @Override
    public ArrayList getPosts()
    {
        return posts;
    }

    @Override
    public boolean setPosts(ArrayList posts)
    {
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

    protected void notifyListenersOfDataChange()
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
	public void refreshPosts() throws UnsuccessfulRefreshException
	{
		this.posts.clear();
		AWSPersistenceManager perMan = new AWSPersistenceManager(this);
        perMan.getPosts();

	}

    @Override
    public boolean fetchMore() throws Exception {
        return false;
    }
}
