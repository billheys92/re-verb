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
    private static final String baseURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/jacob_test/Reverb.php";

    public static boolean getNearbyRegions(GetNearbyRegionsDto dto)
    {
        String params = "?commandtype=get&command=getRegions";
        return requestJson(dto ,Request.Method.POST, baseURL + params);
    }

    public static boolean getSubscribedRegions(GetSubscribedRegionsDto dto)
    {
        String params = "?commandtype=get&command=getRegions";
        return requestJson(dto ,Request.Method.GET, baseURL + params);
    }

    public static boolean getRegionById(GetRegionByIdDto getRegionByIdDto)
    {
        String params = "?commandtype=get&command=getRegion";
        return requestJson(getRegionByIdDto ,Request.Method.GET, baseURL + params);
    }

    public static boolean submitNewRegion(CreateRegionDto regionDto)
    {
        String params = "?commandtype=post&command=postRegion";
        return requestJson(regionDto, Request.Method.PUT, baseURL + params);
    }

    public static boolean followRegion(FollowRegionDto followRegionDto)
    {
        String params = "?commandtype=post&command=postRegion2Users";
        return requestJson(followRegionDto, Request.Method.PUT, baseURL + params);
    }
}
