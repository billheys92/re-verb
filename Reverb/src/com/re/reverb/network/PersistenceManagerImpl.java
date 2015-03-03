package com.re.reverb.network;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PersistenceManagerImpl implements PersistenceManager
{
    protected static final String baseURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/bill_test/Reverb.php";

    private static final RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();

    public static void requestJsonGet(Response.Listener<JSONObject> listener, final String url){
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                Log.d("Reverb", "Error during JSON Object GET request at "+url);
            }
        };
        JsonObjectRequest jsonRequest = new JsonObjectRequest(url, null, listener, errorListener);
        queue.add(jsonRequest);
    }

    public static void requestJson(Object request, int requestType, String url)
    {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                System.out.println("Response is: " + response.toString());
            }
        };

        requestJson(listener, request, requestType, url);
    }

    public static void requestJson(Response.Listener<JSONObject> listener, final Object request, int requestType, String url)
    {
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Gson gson = new Gson();
                String jsonObjectString = gson.toJson(request);
                error.printStackTrace();
                System.out.println("Error Sending Message With: " + jsonObjectString);
            }
        };

        requestJson(listener, errorListener, request, requestType, url);
    }

    public static void requestJson(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Object request, int requestType, String url)
    {
        JSONObject jsonObject = null;
        Gson gson = new Gson();

        String jsonObjectString = gson.toJson(request);

        try
        {
            jsonObject = new JSONObject(jsonObjectString);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        //TODO: Pass in Listener from the extension of the manager
        JsonObjectRequest jsonRequest = new JsonObjectRequest(requestType, url, jsonObject, listener, errorListener);

        queue.add(jsonRequest);
    }

    public static boolean requestJsonArray(Response.Listener<JSONArray> listener, String url)
    {
        JsonArrayRequest jsonRequest = new JsonArrayRequest(url, listener, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                System.out.println("Error Getting Message");
            }
        });

        queue.add(jsonRequest);
        return true;
    }
}
