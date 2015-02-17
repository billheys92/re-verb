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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class RegionManagerImpl extends PersistenceManagerImpl
{
    private static final String baseURL = "http://ec2-54-209-100-107.compute-1.amazonaws.com/test/test/Reverb.php";

    public static void getNearbyRegions()
    {

    }

    public static void getSubscribedRegions()
    {

    }

    public static boolean submitNewRegion(CreateRegionDto regionDto)
    {
        String params = "?commandtype=post&command=postRegion";
        return requestJson(regionDto, Request.Method.PUT, baseURL + params);
    }
}
