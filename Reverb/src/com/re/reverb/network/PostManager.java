package com.re.reverb.network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.re.reverb.androidBackend.feed.Feed;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.post.dto.CreatePostDto;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;

public class PostManager extends PersistenceManagerImpl
{
    private Feed feed;

    public PostManager(Feed feed)
    {
        this.feed = feed;
    }

    public Collection<Post> getPosts(double latitude, double longitude)
    {
        Collection<Post> results = new ArrayList<Post>();
        return results;
    }

    public Collection<Post> getPosts()
    {
        Collection<Post> results = new ArrayList<Post>();
        //TODO: figure out how GET is uniquely identified by server. Path Params? Query Params? JSON?
        return results;
    }

    public boolean submitPost(CreatePostDto postDto)
    {
        return requestJson(postDto, Request.Method.PUT);
    }
}
