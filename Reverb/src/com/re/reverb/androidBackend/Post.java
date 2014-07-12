package com.re.reverb.androidBackend;

import java.sql.Timestamp;
import java.util.UUID;

public class Post
{
	
	private UUID userId;
	private UUID postId;
	private Location postLacation;
	private Timestamp timeCreated;
	private Timestamp timeReceivedByServer;
	private PostContent content;
	
	public Post(UUID userId, PostContent content){
		this.userId = userId;
		this.postId = UUID.randomUUID();
		this.postLacation = new Location();	//only temporary
		this.timeCreated = new Timestamp(System.currentTimeMillis());
		this.content = content;
	}
	
	public PostContent getPostContent(){
		return content;
	}
	
	public void setPostContent(PostContent content){
		this.content = content;
	}

	public UUID getUserId()
	{
		return userId;
	}

	public UUID getPostId()
	{
		return postId;
	}

	public Location getPostLacation()
	{
		return postLacation;
	}
	
	/** 
	 * 
	 * @return the "best" timestamp. 
	 * timeReceivedByServer is set with the server's time, whereas
	 * timeCreated is set by the local system time. In order to
	 * synchronize across multiple devices, we should use the server time,
	 * but in case that hasn't been set, this will return the timeCreated.
	 */
	public Timestamp getTimestamp(){
		if(timeReceivedByServer == null)
		{
			return timeCreated;
		}
		else
		{
			return timeReceivedByServer;
		}
	}
	
	

}
