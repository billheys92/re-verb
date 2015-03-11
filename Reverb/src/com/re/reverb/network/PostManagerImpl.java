package com.re.reverb.network;

import android.app.Activity;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.re.reverb.androidBackend.Feed;
import com.re.reverb.androidBackend.errorHandling.InvalidPostException;
import com.re.reverb.androidBackend.feed.AbstractFeed;
import com.re.reverb.androidBackend.feed.UserPostFeed;
import com.re.reverb.androidBackend.post.ChildPost;
import com.re.reverb.androidBackend.post.ParentPost;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.post.PostFactory;
import com.re.reverb.androidBackend.post.content.PostContent;
import com.re.reverb.androidBackend.post.content.StandardPostContent;
import com.re.reverb.androidBackend.post.dto.CreatePostDto;
import com.re.reverb.androidBackend.post.dto.CreateReplyPostDto;
import com.re.reverb.androidBackend.post.dto.CreateRepostDto;
import com.re.reverb.androidBackend.post.dto.PostActionDto;
import com.re.reverb.androidBackend.post.dto.PostActionResponseDto;
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

    public static void getPosts(final AbstractFeed feed)
    {
        String lastUpdate = feed.getLastPostTime();
        String params = String.format("?commandtype=get&command=getAllMessages&lastupdate='%s'",lastUpdate);
        getPosts(feed, params);
    }

    public static void getPosts(double latitude, double longitude, float range, final AbstractFeed feed)
    {
        String lastUpdate = feed.getEarliestPostTime();
        String params = String.format("?commandtype=get&command=getMessagesByLocationPaging&lat=%s&lon=%s&range=%s&lastupdate='%s'",Double.toString(latitude), Double.toString(longitude), range, lastUpdate);
        getPosts(feed, params);
    }

    public static void getRepost(Integer messageIds, final AbstractFeed feed)
    {
        String params = String.format("?commandtype=get&command=getMessage&message=%s",messageIds);
        getPosts(feed, params);
    }

    public static void getRefreshPosts(double latitude, double longitude, float range, final AbstractFeed feed)
    {
        String lastUpdate = feed.getLastPostTime();
        String params = String.format("?commandtype=get&command=getMessagesByLocationUpdateToLatest&lat=%s&lon=%s&range=%s&lastupdate='%s'",Double.toString(latitude), Double.toString(longitude), range, lastUpdate);
        getPosts(feed, params);
    }

    public static void getPostsForRegion(int regionId, final AbstractFeed feed)
    {
        String params = String.format("?commandtype=get&command=getMessagesByRegion&region=%s",Integer.toString(regionId));
        getPosts(feed, params);
    }

    public static void getPostsForUser(int userId, final UserPostFeed feed)
    {
        String params = String.format("?commandtype=get&command=getMessagesByUser&user=%s", Integer.toString(userId));
        String url = baseURL + params;

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                System.out.println("Response is: "+ response);
                ArrayList<ParentPost> returnedPosts = new ArrayList<ParentPost>();
                for(int i = 0; i < response.length(); i++){
                    try {
                        Gson gson = new Gson();
                        ReceivePostDto postDto = gson.fromJson(response.get(i).toString(), ReceivePostDto.class);
                        ParentPost p = PostFactory.createParentPost(postDto);

                        if(!feed.getPosts().contains(p))
                        {
                            returnedPosts.add(p);
                        }
                        else
                        {
                            //remove old copy and add new
                            if(feed.getPosts().remove(p))
                            {
                                returnedPosts.add(p);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InvalidPostException e) {
                        e.printStackTrace();
                    }
                }
                feed.getPosts().addAll(0, returnedPosts);
                Collections.sort(feed.getPosts());
                feed.notifyListenersOfDataChange();
                if(feed.getPosts().get(0) != null) {
                    feed.setLastPostTime((feed.getPosts().get(0)).getLatestTime());
                }
                if(feed.getPosts().size() > 0) {
                    feed.setEarliestPostTime((feed.getPosts().get(feed.getPosts().size()-1)).getLatestTime());
                }
            }
        };

        requestJsonArray(listener, url);
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

    public static void getPosts(final AbstractFeed feed, final String params)
    {
        String url = baseURL + params;

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                System.out.println("Response is: "+ response);


                ArrayList<ParentPost> returnedPosts = new ArrayList<ParentPost>();
                ArrayList<Integer> reposts = new ArrayList<Integer>();
                for(int i = 0; i < response.length(); i++){
                    try {
                        Gson gson = new Gson();
                        ReceivePostDto postDto = gson.fromJson(response.get(i).toString(), ReceivePostDto.class);
                        if(postDto.getRepost_link() != 0)
                        {
                            reposts.add(postDto.getRepost_link());
                        }
                        else
                        {
                            ParentPost p = PostFactory.createParentPost(postDto);

                            if (!feed.getPosts().contains(p))
                            {
                                returnedPosts.add(p);
                            } else
                            {
                                //remove old copy and add new
                                if (feed.getPosts().remove(p))
                                {
                                    returnedPosts.add(p);
                                }
                            }
                        }
                        for(int j = 0; j < reposts.size(); j++)
                        {
                            getRepost(reposts.get(j), feed);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InvalidPostException e) {
                        e.printStackTrace();
                    }
                }
                feed.getPosts().addAll(0, returnedPosts);
                Collections.sort(feed.getPosts());
                feed.notifyListenersOfDataChange();
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

    public static void getPostReplies(final AbstractFeed feed, final ParentPost post, final
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
                        ChildPost p = PostFactory.createChildPost(postDto);

                        if(!post.getChildPosts().contains(p))
                        {
                            post.getChildPosts().add(p);
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
                feed.notifyListenersOfDataChange();
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

    public static void submitRepost(CreateRepostDto repostDto)
    {
        String params = "?commandtype=post&command=postMessageRepost";
        requestJson(repostDto, Request.Method.POST, baseURL + params);
    }

    public static void submitReplyPost(final CreateReplyPostDto replyPostDto, File image)
    {
        RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();

        MultipartRequest multiRequest = new MultipartRequest("http://ec2-54-209-100-107.compute-1.amazonaws.com/colin_test/uploadimage.php", image, "", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // public void onResponse(String response) {
                // Display the response string.
                if (response.contains("Error"))
                {
                    System.out.println("Picture Response Error is: "+ response);
                }
                else
                {
                    replyPostDto.Picture_name = response;
                    String params = "?commandtype=post&command=postMessageReplyText";
                    requestJson(replyPostDto, Request.Method.PUT, baseURL + params);
                }

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

    public static void submitFavoritePost(PostActionDto postActionDto, final StandardPostContent postContent, final TextView voteCount)
    {
    
        String params = "?commandtype=put&command=updateMessageUpVote";
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                Gson gson = new Gson();
                PostActionResponseDto postActionResponse = gson.fromJson(response.toString(), PostActionResponseDto.class);
                postContent.setNumVotes(postContent.getNumVotes() + postActionResponse.increment);
                voteCount.setText(postContent.getNumVotes().toString().equals("0") ? " " : postContent.getNumVotes().toString());
            }
        };

        requestJson(listener, postActionDto, Request.Method.PUT, baseURL + params);
    }

    public static void submitReportPost(PostActionDto postActionDto, final Activity activity)
    {
        String params = "?commandtype=put&command=updateMessageSpam";
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                Gson gson = new Gson();
                PostActionResponseDto postActionResponse = gson.fromJson(response.toString(), PostActionResponseDto.class);
                if(postActionResponse.db_response == 100)
                {
                    Toast.makeText(activity.getApplicationContext(), "Message Reported", Toast.LENGTH_SHORT).show();
                }
            }
        };

        requestJson(listener, postActionDto, Request.Method.PUT, baseURL + params);
    }

    public static void submitPost(final CreatePostDto postDto, File image)
    {
        RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();

        MultipartRequest multiRequest = new MultipartRequest("http://ec2-54-209-100-107.compute-1.amazonaws.com/colin_test/uploadimage.php", image, "", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // public void onResponse(String response) {
                // Display the response string.
                System.out.println("Picture Sent!");
                if (response.contains("Error"))
                {
                    System.out.println("Picture Response Error is: "+ response);
                }
                else
                {
                    postDto.Picture_name = response;
                    String params = "?commandtype=post&command=postMessagePicture";
                    requestJson(postDto, Request.Method.PUT, baseURL + params);
                }

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

    public static void deletePost(final PostActionDto postActionDto, Activity activity)
    {
        //TODO:
    }
}
