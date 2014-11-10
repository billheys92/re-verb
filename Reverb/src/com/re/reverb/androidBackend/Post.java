package com.re.reverb.androidBackend;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post
{

	private int userId;
	private int postId = -1;
	private Location postLocation;
	private Date timeCreated;
	private PostContent content;
    private boolean anonymous;

    public void setPostLocation(Location postLocation) {
        this.postLocation = postLocation;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public PostContent getContent() {
        return content;
    }

    public void setContent(PostContent content) {
        this.content = content;
    }

    public boolean isAnonymous() {
        return anonymous;
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

	public Date getTimeCreated(){
		return timeCreated;
	}

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean getAnonymous(){
        return anonymous;
    }

    public int getNumProperties() {
        return 6;
    }

    public String getPostPropertyAtIndex(int index) {
        switch(index) {
            case 0:
                return "User ID = "+userId;
            case 1:
                return "Post ID = "+postId;
            case 2:
                return "Latitude = "+postLocation.getLatitude();
            case 3:
                return "Longitude = "+postLocation.getLongitude();
            case 4:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd.hhmmss");
                return "Time = "+sdf.format(timeCreated);
            case 5:
                return "Anonymous = "+anonymous;
            default:
                return "invalid post property index";
        }
    }
}
