package com.re.reverb.network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.errorHandling.InvalidPostException;
import com.re.reverb.androidBackend.feed.Feed;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.post.PostFactory;
import com.re.reverb.androidBackend.post.dto.CreatePostDto;
import com.re.reverb.androidBackend.post.dto.ReceivePostDto;
import com.re.reverb.androidBackend.regions.dto.CreateRegionDto;
import com.re.reverb.androidBackend.regions.dto.FollowRegionDto;
import com.re.reverb.androidBackend.regions.dto.GetNearbyRegionsDto;
import com.re.reverb.androidBackend.regions.dto.GetRegionByIdDto;
import com.re.reverb.androidBackend.regions.dto.GetSubscribedRegionsDto;

import org.json.JSONArray;
import org.json.JSONException;

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

    public static void followRegion(Response.Listener<JSONObject> listener, FollowRegionDto followRegionDto)
    {
        String params = "?commandtype=post&command=postRegion2Users";
        requestJson(listener, followRegionDto, Request.Method.PUT, baseURL + params);
    }

    public static void unfollowRegion(Response.Listener<JSONObject> listener, UnfollowRegionDto unfollowRegionDto)
    {
        String params = "?commandtype=post&command=deleteRegion2Users";
        requestJson(listener, unfollowRegionDto, Request.Method.PUT, baseURL + params);
    }
}
