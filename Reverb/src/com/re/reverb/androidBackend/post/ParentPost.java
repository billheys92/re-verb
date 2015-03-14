package com.re.reverb.androidBackend.post;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.post.content.PostContent;

import java.util.ArrayList;
import java.util.Date;

public class ParentPost extends Post implements Comparable<ParentPost>
{
    private final int regionId;
    private int numReplys;
    private int numReposts;
    private ArrayList<ChildPost> childPosts = new ArrayList<ChildPost>();

    public ParentPost(int regionId,
                      int numReplys,
                      int numReposts,
                      ArrayList<ChildPost> childPosts,
                      int userId,
                      int postId,
                      Location postLocation,
                      Date timeCreated,
                      Date timeUpdated,
                      PostContent content,
                      boolean anonymous)
    {
        super(userId, postId, postLocation, timeCreated, timeUpdated, content, anonymous);
        this.regionId = regionId;
        this.numReplys = numReplys;
        this.numReposts = numReposts;
        this.childPosts = childPosts;
    }

    public int getRegionId()
    {
        return regionId;
    }

    public Integer getNumReplys()
    {
        return numReplys;
    }

    public void setNumReplys(int numReplys)
    {
        this.numReplys = numReplys;
    }

    public Integer getNumReposts()
    {
        return numReposts;
    }

    public void setNumReposts(int numReposts)
    {
        this.numReposts = numReposts;
    }

    public ArrayList<ChildPost> getChildPosts()
    {
        return childPosts;
    }

    public void setChildPosts(ArrayList<ChildPost> childPosts)
    {
        this.childPosts = childPosts;
    }

    @Override
    public int compareTo(ParentPost another)
    {
        boolean test = true;
        if(this.getTimeCreated() != null && this.getTimeCreated().before(another.getTimeCreated()))
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }
}
