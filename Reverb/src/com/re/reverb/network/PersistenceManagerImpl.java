package com.re.reverb.network;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

public class PersistenceManagerImpl implements PersistenceManager
{
    private static final String getPostsURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/querymessagemysqljson.php";
    private static final String sendPostsURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/messagemysqljson.php";

    private static final RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();

    public boolean requestJson(Object request, int requestType)
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
        JsonObjectRequest jsonRequest = new JsonObjectRequest(requestType, sendPostsURL, jsonObject, new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {
                // public void onResponse(String response) {
                // Display the response string.
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
}
