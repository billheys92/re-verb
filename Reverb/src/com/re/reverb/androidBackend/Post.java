package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.PostContent;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Post
{

	private int userId;
	private int postId = -1;
	private Location postLocation;
	private Timestamp timeCreated;
	private Timestamp timeReceivedByServer;
	private PostContent content;
	private List<String> postProperties = new ArrayList<String>();
    private boolean anonymous;

	public Post(int userId){
		this.userId = userId;
        this.anonymous = true;

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

	public int getUserId()
	{
		return userId;
	}

	public int getPostId()
	{
		return postId;
	}

	public Location getPostLocation()
	{
		return postLocation;
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

    public boolean isComplete() {
        if(this.getPostContent() == null || this.getPostContent().isEmpty())
        {
            return false;
        }
        else if(this.getPostId() == -1)
        {
            return false;
        }
        else if(this.getPostLocation() == null)
        {
            return false;
        }
        return true;
    }

    public Collection<String> getIncompleteFieldList() {
        Collection<String> fields = new ArrayList<String>();
        if(this.getPostId() == -1) fields.add("Id");
        if(this.getPostContent() == null || this.getPostContent().isEmpty()) fields.add("Content");
        if(this.getPostLocation() == null) fields.add("Location");

        return fields;
    }
}
