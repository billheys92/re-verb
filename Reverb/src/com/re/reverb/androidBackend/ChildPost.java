package com.re.reverb.androidBackend;

import java.util.Date;

public class ChildPost extends Post
{
    private final int parentId;

    public ChildPost(int parentId,
                     int userId,
                     int postId,
                     Location postLocation,
                     Date timeCreated,
                     PostContent content,
                     boolean anonymous)
    {
        super(userId, postId, postLocation, timeCreated, content, anonymous);
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }
}
