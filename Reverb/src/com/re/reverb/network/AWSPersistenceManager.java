package com.re.reverb.network;

import android.text.format.Time;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.re.reverb.androidBackend.Feed;
import com.re.reverb.androidBackend.PersistenceManager;
import com.re.reverb.androidBackend.Post;
import com.re.reverb.androidBackend.Region;
import com.re.reverb.androidBackend.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Vector;

/**
 * Created by Bill on 2014-10-05.
 */
public class AWSPersistenceManager implements PersistenceManager{

    private static final String getPostsURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/querymessagemysqljson.php";
    private static final String sendPostsURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/messagemysqljson.php";

    private Feed feed;

    public AWSPersistenceManager(){}
    public AWSPersistenceManager(Feed feed){
        this.feed = feed;
    }

    @Override
    public Collection<Post> getPosts(float latitude, float longitude) {
        return null;
    }

    @Override
    public Collection<Post> getPosts() {
        RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();

        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.GET, getPostsURL, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("Response is: "+ response);


                        Vector<String> postStrings = new Vector<String>();
                        for(int i = 0; i < response.length(); i++){
                            try {
                                postStrings.add((new JSONObject(response.get(i).toString())).getString("Message_body"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        feed.handleResponse(postStrings);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Network Error");
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
        return null;
    }

    @Override
    public Collection<Post> getPosts(Region region) {
        return null;
    }

    @Override
    public boolean submitPost(Post post) {
        Time time = new Time();
        time.setToNow();
        GsonPost gsonPost = new GsonPost(2, "Test message " + time.format("%Y-%m-%d %H:%M:%S"), 0, 10.0f, -20.0f,
                time.format("%Y-%m-%d %H:%M:%S"), "NULL",0,0,"NULL","NULL");
        Gson gson = new Gson();
        String jsonPost = gson.toJson(gsonPost);
        System.out.println(jsonPost);

        String url = null;
        try {
            url = sendPostsURL + "?json_message=" + URLEncoder.encode(jsonPost, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(url);


        RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();
        //String url ="http://www.google.com";
        //url = "http://ec2-54-209-100-107.compute-1.amazonaws.com/querymysql.php";

        //Request a string response from the provided URL.
        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.GET, url, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        // public void onResponse(String response) {
                        // Display the response string.
                        System.out.println("Response is: "+ response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error Sending Message");
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
        return true;
    }
    @Override
    public boolean submitPostToRegion(Post post, Region region) {
        return false;
    }

    @Override
    public UserProfile getUserProfileFromLogin(String email, String password) {
        return null;
    }

    @Override
    public void saveUserProfile(UserProfile user) {

    }

    @Override
    public boolean addNewUserProfile(UserProfile user) {
        return false;
    }
}
