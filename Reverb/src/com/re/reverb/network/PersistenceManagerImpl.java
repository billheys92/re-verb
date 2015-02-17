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
    private static final RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();

    public  static boolean requestJson(Object request, int requestType, String url)
    {
        Gson gson = new Gson();
        String jsonObjectString = gson.toJson(request);
        JSONObject jsonObject = null;

        try
        {
            jsonObject = new JSONObject(jsonObjectString);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        //TODO: Pass in Listener from the extension of the manager
        JsonObjectRequest jsonRequest = new JsonObjectRequest(requestType, url, jsonObject, new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {
                System.out.println("Response is: "+ response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                System.out.println("Error Sending Message");
            }
        });

        queue.add(jsonRequest);
        return true;
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
