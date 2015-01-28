package com.re.reverb.network;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.re.reverb.androidBackend.feed.Feed;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.post.PostFactory;
import com.re.reverb.androidBackend.post.dto.ReceivePostDto;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.androidBackend.account.UserProfile;
import com.re.reverb.androidBackend.errorHandling.InvalidPostException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Bill on 2014-10-05.
 */
public class AWSPersistenceManager
{
    private static final String getPostsURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/querymessagemysqljson.php";
    private static final String sendPostsURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/messagemysqljson.php";

    private PostManagerImpl postManagerImpl;

    private Feed feed;

    public AWSPersistenceManager(){}
    public AWSPersistenceManager(Feed feed){
        this.feed = feed;
    }

    public PostManagerImpl getPostManagerImpl()
    {
        return postManagerImpl;
    }

    public Collection<Post> getPosts(float latitude, float longitude) {
        return null;
    }

    public Collection<Post> getPosts() {
        RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();

        JsonArrayRequest jsonRequest = new JsonArrayRequest(getPostsURL, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("Response is: "+ response);


                        ArrayList<Post> returnedPosts = new ArrayList<Post>();
                        for(int i = 0; i < response.length(); i++){
                            try {
                                Gson gson = new Gson();
                                ReceivePostDto gsonPost = gson.fromJson(response.get(i).toString(), ReceivePostDto.class);
                                Post p = PostFactory.createPost(gsonPost);
                                returnedPosts.add(p);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (InvalidPostException e) {
                                e.printStackTrace();
                            }
                        }
                        feed.setPosts(returnedPosts);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Network Error");
                    }
                });
        queue.add(jsonRequest);
        return null;
    }

    public Collection<Post> getPosts(Region region) {
        return null;
    }

    public boolean submitPost(Post post) {

        ReceivePostDto gsonPost = new ReceivePostDto(post);
        Gson gson = new Gson();
        String jsonPost = gson.toJson(gsonPost);
        System.out.println(jsonPost);

        String url = null;
//        try {
//            url = sendPostsURL + "?json_message=" + URLEncoder.encode(jsonPost, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        url = sendPostsURL;
        System.out.println(url);


        RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();
        //String url ="http://www.google.com";
        //url = "http://ec2-54-209-100-107.compute-1.amazonaws.com/querymysql.php";

        //Request a string response from the provided URL.
        JSONObject postJsonObject = null;
        try {
            postJsonObject = new JSONObject(jsonPost);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url, postJsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // public void onResponse(String response) {
                        // Display the response string.
                        System.out.println("Response is: "+ response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        System.out.println("Error Sending Message");
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
        return true;
    }

    public boolean submitPostToRegion(Post post, Region region) {
        return false;
    }

    public UserProfile getUserProfileFromLogin(String email, String password) {
        return null;
    }

    public void saveUserProfile(UserProfile user) {

    }

    public boolean addNewUserProfile(UserProfile user) {
        return false;
    }
}
