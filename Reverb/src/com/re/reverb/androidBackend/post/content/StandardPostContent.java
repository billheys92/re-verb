package com.re.reverb.androidBackend.post.content;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.re.reverb.R;
import com.re.reverb.androidBackend.errorHandling.EmptyPostException;
import com.re.reverb.androidBackend.errorHandling.InvalidPostDataTypeException;

public class StandardPostContent implements PostContent
{
    private String username;
    private String handle;
    private String postBody;
    private Integer numVotes;
    private Integer numReplies;
    private Bitmap profilePicture;

    public StandardPostContent(String username,
                               String handle,
                               String postBody,
                               Integer numVotes,
                               Integer numReplies,
                               Bitmap profilePicture)
    {
        this.username = username;
        this.handle = handle;
        this.postBody = postBody;
        this.numVotes = numVotes;
        this.numReplies = numReplies;
        this.profilePicture = profilePicture;
    }

    public StandardPostContent(Context context, String postBody)
    {
        this.profilePicture = BitmapFactory.decodeResource(context.getResources(), R.drawable.anonymous_pp);
        this.username = "Christopher";
        this.handle = "@chris";
        this.numVotes = 40;
        this.numReplies = 0;
        this.postBody = postBody;
    }

    @Override
    public Object getPostData() throws EmptyPostException
    {
        return postBody;
    }

    @Override
    public void setPostData(Object o) throws InvalidPostDataTypeException
    {

    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public String getMessageString()
    {
        return null;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getHandle()
    {
        return handle;
    }

    public void setHandle(String handle)
    {
        this.handle = handle;
    }

    public Integer getNumVotes()
    {
        return numVotes;
    }

    public void setNumVotes(Integer numVotes)
    {
        this.numVotes = numVotes;
    }

    public Integer getNumReplies()
    {
        return numReplies;
    }

    public void setNumReplies(Integer numReplies)
    {
        this.numReplies = numReplies;
    }

    public String getPostBody()
    {
        return postBody;
    }

    public void setPostBody(String postBody)
    {
        this.postBody = postBody;
    }

    public Bitmap getProfilePicture()
    {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture)
    {
        this.profilePicture = profilePicture;
    }
}
