package com.re.reverb.androidBackend.feed;

import android.content.Context;

import com.re.reverb.androidBackend.OnFeedDataChangedListener;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.androidBackend.post.NewPostFactory;
import com.re.reverb.androidBackend.post.ParentPost;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.network.AWSPersistenceManager;
import com.re.reverb.network.PostManagerImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NewPostFeed implements Feed<ParentPost>
{
    protected static final int FEED_SIZE = 10;
    protected int queuePosition = 0;
    private List<OnFeedDataChangedListener> onDataChangedListeners = new ArrayList<OnFeedDataChangedListener>();
    protected ArrayList<ParentPost> posts = new ArrayList<ParentPost>();
    private Context context;

    public NewPostFeed(Context context)
    {
        this.context = context;
    }

    @Override
    public void init() throws UnsuccessfulRefreshException
    {
        if(this.posts.size() == 0)
        {
            refreshPosts();
        }
    }

    @Override
    public ArrayList<ParentPost> getPosts()
    {
        return posts;
    }

    @Override
    public boolean setPosts(ArrayList<ParentPost> posts){
        if(posts != null && posts.size() != 0) {
            this.posts = posts;
            Collections.reverse(posts);
            notifyListenersOfDataChange();
            return true;
        }
        else {
            return false;
        }
    }

    protected void notifyListenersOfDataChange()
    {
        for(OnFeedDataChangedListener l : onDataChangedListeners) {
            l.onDataChanged();
        }
    }

    @Override
    public void setOnDataChangedListener(OnFeedDataChangedListener listener)
    {
        this.onDataChangedListeners.add(listener);
    }

    @Override
    public void refreshPosts() throws UnsuccessfulRefreshException {
        this.posts.clear();

        NewPostFactory factory = new NewPostFactory(context);

        //Fake Posts
        /*
        ArrayList<ParentPost> testParents = new ArrayList<ParentPost>();

        for(int i = 0; i < 50; i++)
        {
            ParentPost generatedPost = factory.createPost();
            testParents.add(generatedPost);
        }

        this.setPosts(testParents);
        */

        //TODO: Get real posts from the server
        PostManagerImpl.getPosts(this);

        //AWSPersistenceManager persistenceManager = new AWSPersistenceManager(this);
        //persistenceManager.getPosts();
    }

    @Override
    public boolean fetchMore() throws Exception
    {
        return false;
    }
}
