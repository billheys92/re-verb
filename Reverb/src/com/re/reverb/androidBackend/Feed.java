package com.re.reverb.androidBackend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.re.reverb.androidBackend.errorHandling.InvalidPostException;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;

public abstract class Feed
{
	protected static final int FEED_SIZE = 10;
	protected int queuePosition = 0;
    private List<OnFeedDataChangedListener> onDataChangedListeners = new ArrayList<OnFeedDataChangedListener>();
	protected ArrayList<Post> posts = new ArrayList<Post>();

    public void init() throws UnsuccessfulRefreshException{
        if(this.posts.size() == 0)
        {
            refreshPosts();
        }
    }
	
	public ArrayList<Post> getPosts()
	{
		return posts;
	}

	public boolean setPosts(ArrayList<Post> posts){
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

    protected void notifyListenersOfDataChange() {
        for(OnFeedDataChangedListener l : onDataChangedListeners) {
            l.onDataChanged();
        }
    }

    public void setOnDataChangedListener(OnFeedDataChangedListener listener) {
        this.onDataChangedListeners.add(listener);
    }

    /**
     * Clears the post list and re-populates it. When posts are re-set, setPosts must be called
     * @throws UnsuccessfulRefreshException
     */
	public abstract void refreshPosts() throws UnsuccessfulRefreshException;

    /**
     *
     * @return true if more posts were fetched, false if no more were available
     * @throws UnsuccessfulFeedIncrementException
     **/
	public abstract boolean fetchMore() throws UnsuccessfulFeedIncrementException;

}
