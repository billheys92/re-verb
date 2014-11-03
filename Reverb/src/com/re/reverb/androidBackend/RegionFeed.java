package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;

public class RegionFeed extends Feed
{

	private Region region;
	
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
    public boolean fetchMore() throws UnsuccessfulFeedIncrementException {
        return false;
    }

}
