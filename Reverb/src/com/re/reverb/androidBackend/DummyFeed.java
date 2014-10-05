package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.IncompletePostException;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;

public class DummyFeed extends Feed
{
	int maxIndex = FEED_SIZE + queuePosition;
	private int numRefreshes = 0;
	PostFactory postFactory = new PostFactory();
	
	@Override
	public void refreshPosts() throws UnsuccessfulRefreshException
	{
		this.posts.clear();
		for(int i = 0; i < FEED_SIZE; i++){
            try {
                this.posts.add(postFactory.createPost("Post #"+(queuePosition+i)+" refreshed "+numRefreshes+" times",false));
            } catch (NotSignedInException e) {
                e.printStackTrace();
            }
        }
		numRefreshes++;
	}

	@Override
	public void incrementFeed() throws UnsuccessfulFeedIncrementException
	{
        try {
            this.posts.add(postFactory.createPost("Post #"+(maxIndex),false));
        } catch (NotSignedInException e) {
            e.printStackTrace();
        }
        queuePosition++;
		maxIndex++;
	}

    @Override
    public boolean fetchMore() throws UnsuccessfulFeedIncrementException {
        return false;
    }

    public int getNumRefreshes(){
		return numRefreshes;
	}

}
