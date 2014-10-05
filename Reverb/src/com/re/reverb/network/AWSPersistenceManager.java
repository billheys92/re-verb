package com.re.reverb.network;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.re.reverb.androidBackend.PersistenceManager;
import com.re.reverb.androidBackend.Post;
import com.re.reverb.androidBackend.PostFactory;
import com.re.reverb.androidBackend.Region;
import com.re.reverb.androidBackend.UserProfile;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

/**
 * Created by Bill on 2014-10-05.
 */
public class AWSPersistenceManager implements PersistenceManager{

    private static final String getPostsURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/querymessagemysql.php";

    @Override
    public Collection<Post> getPosts(float latitude, float longitude) {
        return null;
    }

    @Override
    public Collection<Post> getPosts() {
        //Instantiate the RequestQueue.
        //RequestQueue queue = RequestQueueSingleton.getInstance(parentActivity.getApplicationContext()).getRequestQueue();
        RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();
        //String url ="http://www.google.com";
        //url = "http://ec2-54-209-100-107.compute-1.amazonaws.com/querymysql.php";

        //Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getPostsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // public void onResponse(String response) {
                        // Display the response string.
                        System.out.println("Response is: "+ response);
                        //feed.messages.get(0).string = response;
                        //feed.updatePosts(response);
                        String result = response;

                        ArrayList<String> postStrings = new ArrayList<String>();
                        postStrings.addAll( Arrays.asList(response.split("\n")) );
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Network Error");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return null;
    }

    @Override
    public Collection<Post> getPosts(Region region) {
        return null;
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
