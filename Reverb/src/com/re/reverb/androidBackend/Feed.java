package com.re.reverb.androidBackend;

import java.util.ArrayList;
import java.util.Vector;

import android.widget.BaseAdapter;

import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;

public abstract class Feed
{
	protected static final int FEED_SIZE = 10;
	protected int queuePosition = 0;
	public BaseAdapter baseAdapter;

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
		this.queuePosition = position;
		refreshPosts();
	}
	
	public void setBaseAdapter(BaseAdapter ba){
		baseAdapter = ba;
	}
	
	public void handleResponse(Vector<String> messages){
		for(int i = 0; i < messages.size(); i++){
        	posts.add( (new SimplePostFactory()).createPost(1,messages.get(i)));
        }
        
        baseAdapter.notifyDataSetChanged();
	}
	
	public abstract void refreshPosts() throws UnsuccessfulRefreshException;
	public abstract void incrementFeed() throws UnsuccessfulFeedIncrementException;

}
