package com.re.reverb.network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.re.reverb.androidBackend.errorHandling.InvalidPostException;
import com.re.reverb.androidBackend.feed.Feed;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.post.PostFactory;
import com.re.reverb.androidBackend.post.dto.CreatePostDto;
import com.re.reverb.androidBackend.post.dto.ReceivePostDto;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;

public class PostManagerImpl extends PersistenceManagerImpl implements PostManager
{
    public static Collection<Post> getPosts(double latitude, double longitude, Feed feed)
    {
        Collection<Post> results = new ArrayList<Post>();
        return results;
    }

    public static Collection<Post> getPosts(final Feed feed)
    {
        Collection<Post> results = new ArrayList<Post>();
        //TODO: figure out how GET is uniquely identified by server. Path Params? Query Params? JSON?


        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                System.out.println("Response is: "+ response);


                ArrayList<Post> returnedPosts = new ArrayList<Post>();
                for(int i = 0; i < response.length(); i++){
                    try {
                        Gson gson = new Gson();
                        ReceivePostDto postDto = gson.fromJson(response.get(i).toString(), ReceivePostDto.class);

                        //TODO: fix create post to create new posts
                        Post p = PostFactory.createPost(postDto);
                        returnedPosts.add(p);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InvalidPostException e) {
                        e.printStackTrace();
                    }
                }
                feed.setPosts(returnedPosts);
            }
        };

        return results;
    }

    public static boolean submitPost(CreatePostDto postDto)
    {
        return requestJson(postDto, Request.Method.PUT);
    }
}
