package com.re.reverb.androidBackend;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Feed
{
	
	protected static final int FEED_SIZE = 10;

	protected Queue<Post> postsQueue;
	
	public Feed(){
		this.postsQueue = new LinkedList<Post>();
	}
	
	public Queue<Post> getAllPosts()
	{
		refreshPosts();
		return postsQueue;
	}
	
	public abstract void refreshPosts();
	public abstract void incrementFeed();

}
