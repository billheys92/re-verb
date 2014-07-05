package com.re.reverb.androidBackend;

import java.util.UUID;

public class DummyFeed extends Feed
{
	int minIndex = 0;
	int maxIndex = FEED_SIZE;
	private int numRefreshes = 0;
	PostFactory postFactory = new SimplePostFactory();

	@Override
	public void refreshPosts()
	{
		numRefreshes++;
		this.postsQueue.clear();
		for(int i = 0; i < FEED_SIZE; i++){
			this.postsQueue.add(postFactory.createPost(UUID.randomUUID(),"Post #"+(minIndex+i)+" - refreshed "+numRefreshes+"times"));
		}
	}

	@Override
	public void incrementFeed()
	{
		// TODO Auto-generated method stub

	}

}
