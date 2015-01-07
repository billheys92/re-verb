package com.re.reverb.androidBackend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.re.reverb.androidBackend.errorHandling.InvalidPostException;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;

public interface Feed<T extends Post>
{

    public void init() throws UnsuccessfulRefreshException;
	
	public ArrayList<T> getPosts();

	public boolean setPosts(ArrayList<T> posts);

    public void setOnDataChangedListener(OnFeedDataChangedListener listener);

    /**
     * Clears the post list and re-populates it. When posts are re-set, setPosts must be called
     * @throws UnsuccessfulRefreshException
     */
	public void refreshPosts() throws UnsuccessfulRefreshException;

    /**
     *
     * @return true if more posts were fetched, false if no more were available
     * @throws UnsuccessfulFeedIncrementException
     **/
	public boolean fetchMore() throws UnsuccessfulFeedIncrementException;

}
