package com.re.reverb.androidBackend;

import android.graphics.Bitmap;

import com.re.reverb.androidBackend.errorHandling.EmptyPostException;
import com.re.reverb.androidBackend.errorHandling.InvalidPostDataTypeException;

public class StandardPostContent implements PostContent
{
    private String username;
    private String handle;
    private String postBody;
    private Bitmap profilePicture;

    public StandardPostContent(String username,
                               String handle,
                               String postBody,
                               Bitmap profilePicture)
    {
        this.username = username;
        this.handle = handle;
        this.postBody = postBody;
        this.profilePicture = profilePicture;
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
