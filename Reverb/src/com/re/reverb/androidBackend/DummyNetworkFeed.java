package com.re.reverb.androidBackend;

import java.util.UUID;

import com.re.reverb.androidBackend.PostFactory;
import com.re.reverb.androidBackend.errorHandling.IncompletePostException;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.network.NetworkRequest;

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
		NetworkRequest nr = new NetworkRequest("http://ec2-54-209-100-107.compute-1.amazonaws.com/querymessagemysql.php", (Feed)this );
        try {
            this.posts.add(postFactory.createPost("Loading New Posts!",false));
        } catch (NotSignedInException e) {
            e.printStackTrace();
        }
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
