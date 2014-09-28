package com.re.reverb.network;

import android.util.Log;

import java.util.Arrays;
import java.util.Vector;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.re.reverb.androidBackend.Feed;

public class NetworkRequest{
	
	public String url;
	public String result;
	public Feed feed;
	
	public NetworkRequest(String url, Feed f) {
        this.url = url;
        this.feed = f;

        //Instantiate the RequestQueue.
        //RequestQueue queue = RequestQueueSingleton.getInstance(parentActivity.getApplicationContext()).getRequestQueue();
        RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();
        //String url ="http://www.google.com";
        //url = "http://ec2-54-209-100-107.compute-1.amazonaws.com/querymysql.php";

        //Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
		    @Override
		    public void onResponse(String response) {
		   // public void onResponse(String response) {
		        // Display the response string.
		        System.out.println("Response is: "+ response);
		        //feed.messages.get(0).string = response;
		        //feed.updatePosts(response);
		        result = response;
		        
		        Vector<String> postStrings = new Vector<String>();
		        postStrings.addAll( Arrays.asList(response.split("\n")) );
		        feed.handleResponse(postStrings);
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	System.out.println("Network Error");
		    }
		});
		// Add the request to the RequestQueue.
		queue.add(stringRequest);
    }
	}
	
//}
