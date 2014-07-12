package com.re.reverb.androidBackend;

import java.util.UUID;

public class DummyFeed extends Feed
{
	int maxIndex = FEED_SIZE + queuePosition;
	private int numRefreshes = 0;
	PostFactory postFactory = new SimplePostFactory();

	@Override
	public void refreshPosts()
	{
		numRefreshes++;
		this.postsQueue.clear();
		for(int i = 0; i < FEED_SIZE; i++){
			this.postsQueue.add(postFactory.createPost(UUID.randomUUID(),"Post #"+(queuePosition+i)));
		}
	}

	@Override
	public void incrementFeed()
	{
		this.postsQueue.add(postFactory.createPost(UUID.randomUUID(), "Post #"+(maxIndex)));
		this.postsQueue.remove();
		queuePosition++;
		maxIndex++;
	}
	
	public int getNumRefreshes(){
		return numRefreshes;
	}

}
