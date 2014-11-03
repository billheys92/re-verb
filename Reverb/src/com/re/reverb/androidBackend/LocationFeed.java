package com.re.reverb.androidBackend;


import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;

public class LocationFeed extends Feed
{
	
	private Location location;
	


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
