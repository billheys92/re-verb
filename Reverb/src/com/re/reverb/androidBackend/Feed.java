package com.re.reverb.androidBackend;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;

public abstract class Feed
{
	protected static final int FEED_SIZE = 10;
	protected int queuePosition = 0;
    private List<OnFeedDataChangedListener> onDataChangedListeners = new ArrayList<OnFeedDataChangedListener>();
	protected ArrayList<Post> posts = new ArrayList<Post>();;
	
	public ArrayList<Post> getPosts() throws UnsuccessfulRefreshException
	{
		if(this.posts.size() == 0)
		{
			refreshPosts();
		}
		return posts;
	}

	public void handleResponse(Vector<String> messages){
		for(int i = 0; i < messages.size(); i++){
            try {
                posts.add( (new PostFactory()).createPost(messages.get(i),false));
            } catch (NotSignedInException e) {
                e.printStackTrace();
            }
        }
        
        notifyListenersOfDataChange();
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
     * Clears the post list and re-populates it.
     * @throws UnsuccessfulRefreshException
     */
	public abstract void refreshPosts() throws UnsuccessfulRefreshException;
	public abstract void incrementFeed() throws UnsuccessfulFeedIncrementException;

    /**
     *
     * @return true if more posts were fetched, false if no more were available
     * @throws UnsuccessfulFeedIncrementException
     **/
	public abstract boolean fetchMore() throws UnsuccessfulFeedIncrementException;

}
