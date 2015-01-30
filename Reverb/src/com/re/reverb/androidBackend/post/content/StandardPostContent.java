package com.re.reverb.androidBackend.post.content;

import android.content.Context;

import com.re.reverb.androidBackend.errorHandling.EmptyPostException;
import com.re.reverb.androidBackend.errorHandling.InvalidPostDataTypeException;

public class StandardPostContent implements PostContent
{
    private static final String profilePictureBaseURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/test/";
    private String username;
    private String handle;
    private String postBody;
    private String profilePictureURL;

    public StandardPostContent(String username,
                               String handle,
                               String postBody,
                               String profilePictureURL)
    {
        this.username = username;
        this.handle = handle;
        this.postBody = postBody;
        this.profilePictureURL = profilePictureURL;
    }

    public StandardPostContent(Context context, String postBody)
    {
        this.profilePictureURL = profilePictureBaseURL + "uploads/test_upload1417126786685";
        this.username = "Christopher";
        this.handle = "@chris";
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

    public String getPostBody()
    {
        return postBody;
    }

    public void setPostBody(String postBody)
    {
        this.postBody = postBody;
    }

    public String getProfilePictureURL()
    {
        return profilePictureBaseURL + profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL)
    {
        this.profilePictureURL = profilePictureURL;
    }
}
