package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.network.AWSPersistenceManager;

import java.util.Collections;

public class DummyNetworkFeed extends Feed
{

	int maxIndex = 0;
	PostFactory postFactory = new PostFactory();
	
	@Override
	public void refreshPosts() throws UnsuccessfulRefreshException
	{
//		this.posts.clear();
//		for(int i = 0; i < FEED_SIZE; i++){
//			this.posts.add(postFactory.createPost(UUID.randomUUID(),"Post #"+(queuePosition+i)));
//		}
//		numRefreshes++;

		this.posts.clear();
		AWSPersistenceManager perMan = new AWSPersistenceManager(this);
        perMan.getPosts();
        notifyListenersOfDataChange();
	}

	@Override
	public void incrementFeed() throws UnsuccessfulFeedIncrementException
	{
//        try {
//            this.posts.add(postFactory.createPost("Post #"+(maxIndex),false));
//        } catch (NotSignedInException e) {
//            e.printStackTrace();
//        }
        queuePosition++;
		maxIndex++;
	}

    @Override
    public boolean fetchMore() throws UnsuccessfulFeedIncrementException {
        return false;
    }
}
