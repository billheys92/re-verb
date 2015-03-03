package com.re.reverb.androidBackend.post;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.post.content.PostContent;

import java.util.ArrayList;
import java.util.Date;

public class ParentPost extends Post
{
    private final int regionId;
    private int numReplys;
    private ArrayList<ChildPost> childPosts = new ArrayList<ChildPost>();

    public ParentPost(int regionId,
                      int numReplys,
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

    public ArrayList<ChildPost> getChildPosts()
    {
        return childPosts;
    }

    public void setChildPosts(ArrayList<ChildPost> childPosts)
    {
        this.childPosts = childPosts;
    }
}
