package com.re.reverb.androidBackend.feed;

import java.util.ArrayList;
import java.util.Date;

import com.re.reverb.androidBackend.OnFeedDataChangedListener;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.androidBackend.post.Post;

public interface Feed<T extends Post>
{

    public void init() throws UnsuccessfulRefreshException;
	
	public ArrayList<T> getPosts();

	public boolean setPosts(ArrayList<T> posts);

    public void setLastPostTime(Date timestamp);

    public String getLastPostTime();

    public void setEarliestPostTime(Date timestamp);

    public String getEarliestPostTime();

    public void setOnDataChangedListener(OnFeedDataChangedListener listener);

    /**
     * Clears the post list and re-populates it. When posts are re-set, setPosts must be called
     * @throws UnsuccessfulRefreshException
     */
	public void refreshPosts() throws UnsuccessfulRefreshException;

    /**
     *
     * @return true if more posts were fetched, false if no more were available
     * @throws java.lang.Exception
     **/
	public boolean fetchMore() throws Exception;

}
