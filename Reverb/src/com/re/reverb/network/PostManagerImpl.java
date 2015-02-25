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

public class PostManagerImpl extends PersistenceManagerImpl implements PostManager
{
    private static final String baseURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/test/test/Reverb.php";

    //TODO: introduce concept of paging for post retrieval

    public static void getPosts(final Feed feed)
    {
        String params = "?commandtype=get&command=getAllMessages";
        getPosts(feed, params);
    }

    public static void getPosts(double latitude, double longitude, float range, final Feed feed)
    {
        String params = String.format("?commandtype=get&command=getMessagesByLocation&lat=%s&lon=%s&range=%s",Double.toString(latitude), Double.toString(longitude), range);
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

                        //TODO: fix create post to create new posts
                        Post p = PostFactory.createParentPost(postDto);
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

        requestJsonArray(listener, url);
    }

    public static boolean submitPost(CreatePostDto postDto)
    {
        String params = "?commandtype=post&command=postMessageText";
        return requestJson(postDto, Request.Method.PUT, baseURL + params);
    }

    public static void submitReplyPost(CreatePostDto postDto)
    {

    }
}
