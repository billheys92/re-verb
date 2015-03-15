package com.re.reverb.network;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.androidBackend.regions.RegionFactory;
import com.re.reverb.androidBackend.regions.dto.CreateRegionDto;
import com.re.reverb.androidBackend.regions.dto.FollowRegionDto;
import com.re.reverb.androidBackend.regions.dto.GetRegionByIdDto;
import com.re.reverb.androidBackend.regions.dto.GetSubscribedRegionsDto;
import com.re.reverb.androidBackend.regions.dto.ReceiveRegionSummaryDto;
import com.re.reverb.androidBackend.regions.dto.UnfollowRegionDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class RegionManagerImpl extends PersistenceManagerImpl
{
    public static void getNearbyRegions(Location location)
    {
        String params = String.format("?commandtype=get&command=getRegionsByLocation&lat=%s&lon=%s",location.getLatitude(),location.getLongitude());
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                System.out.println("Response is: " + response.toString());
                ArrayList<Region> returnedRegions = new ArrayList<Region>();
                for(int i = 0; i < response.length(); i++){
                    try {
                        Gson gson = new Gson();
                        ReceiveRegionSummaryDto regionSummaryDto = gson.fromJson(response.get(i).toString(), ReceiveRegionSummaryDto.class);
                        returnedRegions.add(RegionFactory.createRegionFromSummary(regionSummaryDto));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Reverb.getInstance().getRegionManager().setNearbyRegions(returnedRegions);
            }
        };
        requestJsonArray(listener, baseURL + params);
    }

    public static void getSubscribedRegions(GetSubscribedRegionsDto dto)
    {
//        String params = "?commandtype=get&command=getRegions";
//        requestJson(dto ,Request.Method.GET, baseURL + params);

        String params = String.format("?commandtype=get&command=getRegionsByUser&user=%s",dto.getPoster_id());
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                System.out.println("Response is: " + response.toString());
                ArrayList<Region> returnedRegions = new ArrayList<Region>();
                for(int i = 0; i < response.length(); i++){
                    try {
                        Gson gson = new Gson();
                        ReceiveRegionSummaryDto regionSummaryDto = gson.fromJson(response.get(i).toString(), ReceiveRegionSummaryDto.class);
                        Region region = RegionFactory.createRegionFromSummary(regionSummaryDto);
                        region.subscribe();
                        returnedRegions.add(region);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Reverb.getInstance().getRegionManager().setSubscribedRegions(returnedRegions);
            }
        };
        requestJsonArray(listener, baseURL + params);
    }

    public static void getRegionById(Response.Listener<JSONObject> listener, GetRegionByIdDto getRegionByIdDto)
    {
        String params = "?commandtype=get&command=getRegion";
        requestJson(listener, getRegionByIdDto ,Request.Method.POST, baseURL + params);
    }

    public static void submitNewRegion(CreateRegionDto regionDto)
    {
        String params = "?commandtype=post&command=postRegion";
        requestJson(regionDto, Request.Method.PUT, baseURL + params);
    }

    public static void submitNewRegion(final CreateRegionDto regionDto, File thumbnail)
    {
        RequestQueue queue = RequestQueueSingleton.getInstance().getRequestQueue();

        MultipartRequest multiRequest = new MultipartRequest(uploadImageURL, thumbnail, "", new Response.Listener<String>() {

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
                    regionDto.Picture_name = response;
                    String params = "?commandtype=post&command=postRegion";
                    requestJson(regionDto, Request.Method.PUT, baseURL + params);
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

    public static void followRegion(Response.Listener<JSONObject> listener, FollowRegionDto followRegionDto)
    {
        String params = "?commandtype=post&command=regionSubscriptionToggle";
        requestJson(listener, followRegionDto, Request.Method.POST, baseURL + params);
    }

    public static void unfollowRegion(Response.Listener<JSONObject> listener, UnfollowRegionDto unfollowRegionDto)
    {
        String params = "?commandtype=delete&command=regionSubscriptionToggle";
        requestJson(listener, unfollowRegionDto, Request.Method.POST, baseURL + params);
    }
}
