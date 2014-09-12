package com.re.reverb.androidBackend;

import java.util.UUID;

import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFetchPostsException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulWindowDecrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulWindowIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;

public class DummyFeed extends Feed
{
	
	
	private int numRefreshes = 0;
	PostFactory postFactory = new SimplePostFactory();
	
	@Override
	public void refreshPosts() throws UnsuccessfulRefreshException
	{
		this.posts.clear();
		for(int i = 0; i < INIT_FEED_SIZE; i++){
			try
			{
				fetchMore();
			} catch (UnsuccessfulFetchPostsException e)
			{
				throw new UnsuccessfulRefreshException("Could not fetch posts");
			}
		}
		numRefreshes++;
	}

	@Override
	public void fetchMore() throws UnsuccessfulFetchPostsException
	{
		this.posts.add(postFactory.createPost(UUID.randomUUID(), "Post #"+(updateIndex)));

	}
	
	public int getNumRefreshes(){
		return numRefreshes;
	}

	@Override
	public void decrementWindow() throws UnsuccessfulWindowDecrementException
	{
		// TODO Auto-generated method stub
		
	}

}
