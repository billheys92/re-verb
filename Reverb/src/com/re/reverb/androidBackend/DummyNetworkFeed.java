package com.re.reverb.androidBackend;

import java.util.UUID;

import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.network.NetworkRequest;

public class DummyNetworkFeed extends Feed
{
	
	
	int maxIndex = 0;
	private int numRefreshes = 0;
	PostFactory postFactory = new SimplePostFactory();
	
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
		this.posts.add(postFactory.createPost(UUID.randomUUID(),"Loading New Posts!"));
		numRefreshes++;
	}

	@Override
	public void incrementFeed() throws UnsuccessfulFeedIncrementException
	{
		this.posts.add(postFactory.createPost(UUID.randomUUID(), "Post #"+(maxIndex)));
		queuePosition++;
		maxIndex++;
	}
	
	public int getNumRefreshes(){
		return numRefreshes;
	}

}
