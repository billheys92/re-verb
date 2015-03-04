package com.re.reverb.androidBackend.post;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.post.content.PostContent;

import java.util.Date;

public class ChildPost extends Post implements Comparable<ChildPost>
{
    private final int parentId;

    public ChildPost(int parentId,
                     int userId,
                     int postId,
                     Location postLocation,
                     Date timeCreated,
                     Date timeUpdated,
                     PostContent content,
                     boolean anonymous)
    {
        super(userId, postId, postLocation, timeCreated, timeUpdated, content, anonymous);
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    @Override
    public int compareTo(ChildPost another)
    {
        if(this.getTimeCreated() != null && this.getTimeCreated().before(another.getTimeCreated()))
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }
}
