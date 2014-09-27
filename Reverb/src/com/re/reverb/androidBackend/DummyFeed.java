package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.androidBackend.feed.Feed;

public class DummyFeed extends Feed
{
	int maxIndex = FEED_SIZE + queuePosition;
	private int numRefreshes = 0;
	PostFactory postFactory = new SimplePostFactory();
	
	@Override
	public void refreshPosts() throws UnsuccessfulRefreshException
	{
		this.posts.clear();
		for(int i = 0; i < FEED_SIZE; i++){
			this.posts.add(postFactory.createPost(1,"Post #"+(queuePosition+i)+" refreshed "+numRefreshes+" times"));
		}
		numRefreshes++;
	}

	@Override
	public void incrementFeed() throws UnsuccessfulFeedIncrementException
	{
		this.posts.add(postFactory.createPost(1, "Post #"+(maxIndex)));
		queuePosition++;
		maxIndex++;
	}
	
	public int getNumRefreshes(){
		return numRefreshes;
	}

}
