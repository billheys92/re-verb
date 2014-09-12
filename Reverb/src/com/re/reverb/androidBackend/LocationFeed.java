package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFetchPostsException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulWindowDecrementException;


public class LocationFeed extends Feed
{
	
	private Location location;
	private PostFactory factory = new SimplePostFactory();


	@Override
	public void refreshPosts()
	{
		// TODO Auto-generated method stub

	}


	@Override
	public void fetchMore() throws UnsuccessfulFetchPostsException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decrementWindow() throws UnsuccessfulWindowDecrementException
	{
		// TODO Auto-generated method stub
		
	}

}
