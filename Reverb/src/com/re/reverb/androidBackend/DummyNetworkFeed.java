package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.network.AWSPersistenceManager;

import java.util.Collections;

public class DummyNetworkFeed extends Feed
{

	@Override
	public void refreshPosts() throws UnsuccessfulRefreshException
	{
		this.posts.clear();
		AWSPersistenceManager perMan = new AWSPersistenceManager(this);
        perMan.getPosts();

	}

    @Override
    public boolean fetchMore() throws UnsuccessfulFeedIncrementException {
        return false;
    }
}
