package com.re.reverb.network;

import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.re.reverb.androidBackend.errorHandling.InvalidPostException;
import com.re.reverb.androidBackend.feed.Feed;
import com.re.reverb.androidBackend.feed.NewPostFeed;
import com.re.reverb.androidBackend.post.ChildPost;
import com.re.reverb.androidBackend.post.ParentPost;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.post.PostFactory;
import com.re.reverb.androidBackend.post.dto.CreatePostDto;
import com.re.reverb.androidBackend.post.dto.CreateReplyPostDto;
import com.re.reverb.androidBackend.post.dto.FavoritePostDto;
import com.re.reverb.androidBackend.post.dto.ReceivePostDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class PostManagerImpl extends PersistenceManagerImpl implements PostManager
{
    //TODO: introduce concept of paging for post retrieval

    public static void getPosts(final Feed feed)
    {
        String lastUpdate = feed.getLastPostTime();
        String params = String.format("?commandtype=get&command=getAllMessages&lastupdate='%s'",lastUpdate);
        getPosts(feed, params);
    }

    public static void getPosts(double latitude, double longitude, float range, final Feed feed)
    {
        String lastUpdate = feed.getLastPostTime();
        String params = String.format("?commandtype=get&command=getMessagesByLocation&lat=%s&lon=%s&range=%s&lastupdate='%s'",Double.toString(latitude), Double.toString(longitude), range, lastUpdate);
        getPosts(feed, params);
    }

    public static void getPostsForRegion(int regionId, final Feed feed)
    {

    }

    public static void getPostsForUser(int userId, final Feed feed)
    {

    }

    public static boolean getNumNewPosts(double latitude, double longitude, float range, String lastUpdateTime) {

        String url = baseURL + String.format("?commandtype=get&command=getNumNewMessages&lat=%s&lon=%s&range=%s&lastupdate='%s'",Double.toString(latitude), Double.toString(longitude), range, lastUpdateTime);
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                System.out.println("Response is: "+ response);

            }
        };

        requestJsonGet(listener, url);
        return true;
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
                ((NewPostFeed)feed).notifyListenersOfDataChange();
                if(feed.getPosts().get(0) != null) {
                    feed.setLastPostTime(((Post)feed.getPosts().get(0)).getLatestTime());
                }
                if(feed.getPosts().size() > 0) {
                    feed.setEarliestPostTime(((Post)feed.getPosts().get(feed.getPosts().size()-1)).getLatestTime());
                }

            }
        };

        requestJsonArray(listener, url);
    }

    public static void getPostReplies(final Feed feed, final ParentPost post, final
                                      ExpandableListView listView, final int groupPosition)
    {
        String params = String.format("?commandtype=get&command=getMessageReplies&message=%s", Integer.toString(post.getPostId()));
        String url = baseURL + params;

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                for(int i = 0; i < response.length(); i++){
                    try {
                        Gson gson = new Gson();
                        ReceivePostDto postDto = gson.fromJson(response.get(i).toString(), ReceivePostDto.class);
                        Post p = PostFactory.createChildPost(postDto);

                        if(!post.getChildPosts().contains(p))
                        {
                            post.getChildPosts().add((ChildPost) p);
                        }
                        else
                        {
                            //TODO: update post values
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InvalidPostException e) {
                        e.printStackTrace();
                    }
                }
                Collections.sort(post.getChildPosts());
                ((NewPostFeed)feed).notifyListenersOfDataChange();
                listView.expandGroup(groupPosition);
            }
        };

        requestJsonArray(listener, url);
    }

    public static void submitPost(CreatePostDto postDto)
    {
        String params = "?commandtype=post&command=postMessageText";
        requestJson(postDto, Request.Method.POST, baseURL + params);
    }

    public static void submitReplyPost(CreateReplyPostDto replyPostDto)
    {
        String params = "?commandtype=post&command=postMessageReplyText";
        requestJson(replyPostDto, Request.Method.POST, baseURL + params);
    }

    public static void submitFavoritePost(FavoritePostDto favoritePostDto)
    {
    
        String params = "?commandtype=put&command=updateMessageUpVote";
        requestJson(favoritePostDto, Request.Method.PUT, baseURL + params);
    }

    public static void submitPost(final CreatePostDto postDto, File image)
    {
        RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();

        MultipartRequest multiRequest = new MultipartRequest("http://ec2-54-209-100-107.compute-1.amazonaws.com/colin_test/uploadimage.php", image, "", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // public void onResponse(String response) {
                // Display the response string.
                System.out.println("Picture Response is: "+ response);
                postDto.Picture_name = response;
                String params = "?commandtype=post&command=postMessagePicture";
                requestJson(postDto, Request.Method.PUT, baseURL + params);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Error Sending Picture Message");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(multiRequest);

    }
}
