package com.re.reverb.androidBackend;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Post
{

	private int userId;
	private int postId = -1;
	private Location postLocation;
	private Timestamp timeCreated;
	private PostContent content;
	private List<String> postProperties = new ArrayList<String>();  //only temporary until we get a better post view otg
    private boolean anonymous;

	public Post(){
		this.userId = -1;
        this.anonymous = true;


		postProperties.add("Anonymous = "+anonymous);
        postProperties.add("User id = "+userId);
        postProperties.add("Time created = not implemented yet");
        postProperties.add("Location = not implemented yet");
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

	public Timestamp getTimestamp(){
		return timeCreated;
	}

    public void setUserId(int userId) {
        this.postProperties.set(1,""+userId);
        this.userId = userId;
    }

    public void setAnonymous(boolean anonymous) {
        this.postProperties.set(0,(anonymous ? "true" : "false"));
        this.anonymous = anonymous;
    }

    public boolean getAnonymous(){
        return anonymous;
    }
}
