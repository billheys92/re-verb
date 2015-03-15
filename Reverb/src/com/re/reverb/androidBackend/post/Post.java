package com.re.reverb.androidBackend.post;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.post.content.PostContent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Post
{
    private final int userId;
    private final int postId;
    private final Location postLocation;
    private final Date timeCreated;
    private final Date timeUpdated;
    private final PostContent content;
    private final boolean anonymous;

    public Post(int userId,
                int postId,
                Location postLocation,
                Date timeCreated,
                Date timeUpdated, PostContent content,
                boolean anonymous)
    {
        this.userId = userId;
        this.postId = postId;
        this.postLocation = postLocation;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
        this.content = content;
        this.anonymous = anonymous;
    }

    public PostContent getContent()
    {
        return content;
    }

    public boolean isAnonymous()
    {
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

    public Date getTimeCreated()
    {
        return timeCreated;
    }
    public Date getTimeUpdated()
    {
        return timeUpdated;
    }

    public Date getLatestTime()
    {
        if(timeUpdated != null)
        {
            return timeUpdated;
        }
        else
        {
            return timeCreated;
        }
    }

    public boolean getAnonymous()
    {
        return anonymous;
    }

    public int getNumProperties()
    {
        return 6;
    }

    public String getPostPropertyAtIndex(int index)
    {
        switch (index)
        {
            case 0:
                return "User ID = " + userId;
            case 1:
                return "Post ID = " + postId;
            case 2:
                return "Latitude = " + postLocation.getLatitude();
            case 3:
                return "Longitude = " + postLocation.getLongitude();
            case 4:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd.hhmmss");
                return "Time = " + sdf.format(timeCreated);
            case 5:
                return "Anonymous = " + anonymous;
            default:
                return "invalid post property index";
        }
    }

    @Override
    public boolean equals(Object object)
    {
        boolean equal = false;
        if(object != null && object instanceof Post)
        {
            equal = (this.postId == ((Post) object).postId);

        }

        return equal;
    }
}
