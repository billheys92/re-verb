package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFetchPostsException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulWindowDecrementException;

public class RegionFeed extends Feed
{

	private Region region;
	private PostFactory factory = new SimplePostFactory();

	
	//RegionFeed(Region region)
	//{
	//	super();
	//	this.region = region;
	//}

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
