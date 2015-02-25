package com.re.reverb.network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.re.reverb.androidBackend.errorHandling.InvalidPostException;
import com.re.reverb.androidBackend.feed.Feed;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.post.PostFactory;
import com.re.reverb.androidBackend.post.dto.CreatePostDto;
import com.re.reverb.androidBackend.post.dto.CreateReplyPostDto;
import com.re.reverb.androidBackend.post.dto.ReceivePostDto;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class PostManagerImpl extends PersistenceManagerImpl implements PostManager
{
    //TODO: introduce concept of paging for post retrieval

    public static void getPosts(final Feed feed)
    {
        String params = "?commandtype=get&command=getAllMessages";
        getPosts(feed, params);
    }

    public static void getPosts(double latitude, double longitude, final Feed feed)
    {
        String params = "?commandtype=get&command=getAllMessages&lat=" + Double.toString(latitude) + "&lon=" + Double.toString(longitude);
        getPosts(feed, params);
    }

    public static void getPostsForRegion(int regionId, final Feed feed)
    {

    }

    public static void getPostsForUser(int userId, final Feed feed)
    {

    }

    public static void getPosts(final Feed feed, final String params)
    {
        String url = baseURL + params;

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
                        Post p = PostFactory.createParentPost(postDto);

                        System.out.println(p.getPostId());

                        if(!feed.getPosts().contains(p))
                        {
                            returnedPosts.add(p);
                        }
                        else
                        {
                            //TODO: update values inside post if needed
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InvalidPostException e) {
                        e.printStackTrace();
                    }
                }
                feed.getPosts().addAll(0, returnedPosts);
                Collections.sort(feed.getPosts());
            }
        };

        requestJsonArray(listener, url);
    }

    public static void submitPost(CreatePostDto postDto)
    {
        String params = "?commandtype=post&command=postMessageText";
        requestJson(postDto, Request.Method.PUT, baseURL + params);
    }

    public static void submitReplyPost(CreateReplyPostDto replyPostDto)
    {
        String params = "?commandtype=post&command=postReplyText";
        requestJson(replyPostDto, Request.Method.POST, baseURL + params);
    }
}
