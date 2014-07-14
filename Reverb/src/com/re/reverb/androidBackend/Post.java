package com.re.reverb.androidBackend;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Post
{
	
	private UUID userId;
	private UUID postId;
	private Location postLacation;
	private Timestamp timeCreated;
	private Timestamp timeReceivedByServer;
	private PostContent content;
	private List<String> postProperties = new ArrayList<String>();	//This is just a placeholder 
	
	public Post(UUID userId, PostContent content){
		this.userId = userId;
		this.postId = UUID.randomUUID();
		this.postLacation = new Location();	//only temporary
		this.timeCreated = new Timestamp(System.currentTimeMillis());
		this.content = content;
		
		for(int i = 0; i < 4; i++)
		{
			postProperties.add("Post property "+i);
		}
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
	
	public String getPostPropertyAtIndex(int index) throws ArrayIndexOutOfBoundsException
	{
		return postProperties.get(index);
	}
	
	public int getNumProperties()
	{
		return postProperties.size();
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
