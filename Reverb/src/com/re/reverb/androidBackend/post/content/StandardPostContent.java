package com.re.reverb.androidBackend.post.content;

import android.content.Context;

import com.re.reverb.androidBackend.errorHandling.EmptyPostException;
import com.re.reverb.androidBackend.errorHandling.InvalidPostDataTypeException;

public class StandardPostContent implements PostContent
{
    private static final String profilePictureBaseURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/test/";
    private static final String messageImageBaseURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/";
    private String username;
    private String handle;
    private String postBody;
    private String profilePictureURL;
    private Integer numVotes;
    private Integer numReplies; //get off parent post, replies don't have replies

    private String messageImage;

    public StandardPostContent(String username,
                               String handle,
                               String postBody,
                               Integer numVotes,
                               Integer numReplies,
                               String profilePictureURL,
                               String messageImage)
    {
        this.username = username;
        this.handle = handle;
        this.postBody = postBody;
        this.profilePictureURL = profilePictureURL;
        this.numVotes = numVotes;
        this.numReplies = numReplies;
        this.messageImage = messageImage;
    }

    public StandardPostContent(Context context, String postBody)
    {
        this.profilePictureURL = profilePictureBaseURL + "uploads/test_upload1417126786685";
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

    @Deprecated
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

    public String getProfilePictureURL()
    {
        return profilePictureBaseURL + profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL)
    {
        this.profilePictureURL = profilePictureURL;
    }

    public String getProfilePictureName()
    {
        return  profilePictureURL;
    }

    public String getMessageImage()
    {
        return messageImageBaseURL + messageImage;
    }

    public String getMessageImageName()
    {
        return messageImage;
    }

    public void setMessageImage(String messageImage)
    {
        this.messageImage = messageImage;
    }
}
