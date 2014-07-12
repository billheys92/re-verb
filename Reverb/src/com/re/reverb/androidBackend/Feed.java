package com.re.reverb.androidBackend;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Feed
{
	
	protected static final int FEED_SIZE = 10;
	protected int queuePosition = 0;

	protected Queue<Post> postsQueue;
	
	public Feed(){
		this.postsQueue = new LinkedList<Post>();
	}
	
	public Queue<Post> getAllPosts()
	{
		if(this.postsQueue.size() < FEED_SIZE)
		{
			refreshPosts();
		}
		return postsQueue;
	}
	
	public void setQueuePosition(int position)
	{
		this.queuePosition = position;
		refreshPosts();
	}
	
	public int getQueuePosition(int position)
	{
		return this.queuePosition;
	}
	
	public abstract void refreshPosts();
	public abstract void incrementFeed();

}
