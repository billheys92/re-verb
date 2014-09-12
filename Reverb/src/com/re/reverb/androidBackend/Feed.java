package com.re.reverb.androidBackend;

import java.util.ArrayList;

import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFetchPostsException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulWindowDecrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulWindowIncrementException;

public abstract class Feed
{
	protected static int INIT_FEED_SIZE = 30;
	protected int pointer = 0;
	protected int windowSize = 15;
	protected int updateIndex = pointer + windowSize;

	protected ArrayList<Post> posts;
	
	public Feed()
	{
		this.posts = new ArrayList<Post>();
	}
	
	public ArrayList<Post> getPosts() throws UnsuccessfulRefreshException
	{
		if(this.posts.size() == 0)
		{
			refreshPosts();
		}
		return posts;
	}
	
	public void setQueuePosition(int position) throws UnsuccessfulRefreshException
	{
		this.pointer = position;
		refreshPosts();
	}
	
	public void incrementWindow() throws UnsuccessfulWindowIncrementException
	{
		pointer++;
		updateIndex++;
		if(pointer + windowSize >= updateIndex)
		{
			try
			{
				fetchMore();
			} catch (UnsuccessfulFetchPostsException e)
			{
				throw new UnsuccessfulWindowIncrementException("Failed to fetch more posts");
			}
		}
	}
	
	public abstract void refreshPosts() throws UnsuccessfulRefreshException;
	public abstract void fetchMore() throws UnsuccessfulFetchPostsException;
	public abstract void decrementWindow() throws UnsuccessfulWindowDecrementException;

}
