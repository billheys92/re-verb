package com.re.reverb.androidBackend.feed;

import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.network.PostManagerImpl;

public class UserPostFeed extends AbstractFeed
{
    @Override
    public void refreshPosts() throws UnsuccessfulRefreshException, NotSignedInException
    {
        //TODO: link getUserPosts here
        PostManagerImpl.getPostsForUser(Reverb.getInstance().getCurrentUserId(), this);
    }

    @Override
    public boolean fetchMore() throws Exception, NotSignedInException
    {
        PostManagerImpl.getPostsForUser(Reverb.getInstance().getCurrentUserId(), this);
        return false;
    }
}
