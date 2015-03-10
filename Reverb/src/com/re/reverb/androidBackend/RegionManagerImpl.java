package com.re.reverb.androidBackend;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.post.PostFactory;
import com.re.reverb.androidBackend.post.dto.ReceivePostDto;
import com.re.reverb.androidBackend.regions.CommonsRegion;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.androidBackend.regions.RegionFactory;
import com.re.reverb.androidBackend.regions.RegionImageUrlFactory;
import com.re.reverb.androidBackend.regions.dto.CreateRegionDto;
import com.re.reverb.androidBackend.regions.dto.FollowRegionDto;
import com.re.reverb.androidBackend.regions.dto.GetRegionByIdDto;
import com.re.reverb.androidBackend.regions.dto.GetSubscribedRegionsDto;
import com.re.reverb.androidBackend.regions.dto.ReceiveRegionCircleDto;
import com.re.reverb.androidBackend.regions.dto.ReceiveRegionDto;
import com.re.reverb.androidBackend.regions.dto.ReceiveRegionRectangleDto;
import com.re.reverb.androidBackend.regions.dto.UnfollowRegionDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Bill on 2015-01-11.
 */
public class RegionManagerImpl implements RegionManager, LocationUpdateListener
{
    private Region currentRegion;
    private ArrayList<Region> nearbyRegions;
    private ArrayList<Region> subscribedRegions;

    public RegionManagerImpl(){
        this.nearbyRegions = new ArrayList<Region>();
        this.subscribedRegions = new ArrayList<Region>();

        Reverb.attachLocationListener(this);
    }

    @Override
    public void setCurrentRegion(Region region)
    {
        this.currentRegion = region;
        fetchCurrentRegionDetails();
    }

    public void fetchCurrentRegionDetails()
    {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                Gson gson = new Gson();
                ReceiveRegionDto regionDto = gson.fromJson(jsonObject.toString(), ReceiveRegionDto.class);

                ArrayList<ReceiveRegionCircleDto> circleDtos = new ArrayList<ReceiveRegionCircleDto>();
                ArrayList<ReceiveRegionRectangleDto> rectangleDtos = new ArrayList<ReceiveRegionRectangleDto>();
                try
                {
                    if(jsonObject.getString("circles") != "null")
                    {
                        JSONArray jsonCircles = jsonObject.getJSONArray("circles");
                        for (int i = 0; i < jsonCircles.length(); i++)
                        {
                            JSONObject obj = jsonCircles.getJSONObject(i);
                            ReceiveRegionCircleDto dto = gson.fromJson(obj.toString(), ReceiveRegionCircleDto.class);
                            circleDtos.add(dto);
                        }
                    }
                    if(jsonObject.getString("rectangles") != "null")
                    {
                        JSONArray jsonRectangles = jsonObject.getJSONArray("rectangles");
                        for (int i = 0; i < jsonRectangles.length(); i++)
                        {
                            JSONObject obj = jsonRectangles.getJSONObject(i);
                            ReceiveRegionRectangleDto dto = gson.fromJson(obj.toString(), ReceiveRegionRectangleDto.class);
                            rectangleDtos.add(dto);
                        }
                    }
                    regionDto.setCirclesDto(circleDtos);
                    regionDto.setRectanglesDto(rectangleDtos);
                } catch (JSONException e)
                {
                    Log.e("Reverb","Error parsing JSON string while fetching Region details");
                    e.printStackTrace();
                }
                updateCurrentRegion(RegionFactory.createRegionFromDto(regionDto));
            }
        };
        if(this.currentRegion.getRegionId() > 0)
        {
            GetRegionByIdDto dto = new GetRegionByIdDto(this.currentRegion.getRegionId());
            com.re.reverb.network.RegionManagerImpl.getRegionById(listener, dto);
        }
        else
        {
            updateCurrentRegion(new CommonsRegion(Reverb.getInstance().getCurrentLocation()));
        }

    }

    public void updateCurrentRegion(Region region)
    {
        this.currentRegion = region;
        this.currentRegion.update();
        this.currentRegion.setReadPermission(this.currentRegion.containsPoint(Reverb.getInstance().getCurrentLocation()) || this.subscribedRegions.contains(this.currentRegion));
        this.currentRegion.setWritePermission(this.currentRegion.containsPoint(Reverb.getInstance().getCurrentLocation()));
    }

    @Override
    public Region getCurrentRegion()
    {
        return this.currentRegion;
    }

    @Override
    public ArrayList<Region> getNearbyRegions()
    {
        return this.nearbyRegions;
    }

    @Override
    public ArrayList<Region> getSubscribedRegions()
    {
        return this.subscribedRegions;
    }

    @Override
    public void setNearbyRegions(ArrayList<Region> regions)
    {
        this.nearbyRegions = regions;
        Reverb.notifyAvailableRegionsUpdateListeners();
    }

    @Override
    public void setSubscribedRegions(ArrayList<Region> regions)
    {
        this.subscribedRegions = regions;
        Reverb.notifyAvailableRegionsUpdateListeners();
    }

    @Override
    public ArrayList<String> getNearbyRegionNames()
    {
        ArrayList<String> names = new ArrayList<String>();
        for(Region r: this.getNearbyRegions()) {
            names.add(r.getName());
        }
        return names;
    }

    @Override
    public ArrayList<String> getSubscribedRegionNames()
    {
        ArrayList<String> names = new ArrayList<String>();
        for(Region r: this.getSubscribedRegions()) {
            names.add(r.getName());
        }
        return names;
    }

    @Override
    public void createNewRegion(Region region)
    {
        if(region != null)
        {
            CreateRegionDto regionDto = buildRegionDto(region);
            if(region.getThumbnail() != null)
            {
                com.re.reverb.network.RegionManagerImpl.submitNewRegion(regionDto, region.getThumbnail());
            }
            else
            {
                com.re.reverb.network.RegionManagerImpl.submitNewRegion(regionDto);
            }
            //TODO: add next two lines to listener for submitNewRegion if we want it upon success
            this.nearbyRegions.add(region);
            Reverb.notifyAvailableRegionsUpdateListeners();
        }
    }


    private CreateRegionDto buildRegionDto(Region region) {
        CreateRegionDto regionDto;
        try
        {
            regionDto = new CreateRegionDto(Reverb.getInstance().getCurrentUserId(),
                    region.getName(),
                    region.getDescription(),
                    region.getShapes());

        } catch (NotSignedInException e)
        {
            Log.d("Reverb", "Could not create region because " + e.getMessage());
            return null;
        }
        return regionDto;
    }

    @Override
    public void subscribeToRegion(Region region)
    {
        final Region r = region;
        if (region != null)
        {
            FollowRegionDto followRegionDto;
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject jsonObject)
                {
                    subscribeToRegionCallback(r);
                }
            };
            try {
                followRegionDto = new FollowRegionDto(Reverb.getInstance().getCurrentUserId(), region.getRegionId());
                com.re.reverb.network.RegionManagerImpl.followRegion(listener, followRegionDto);
            }
            catch (NotSignedInException e) {
                e.printStackTrace();
            }
        }
    }

    private void subscribeToRegionCallback(Region region) {
        this.subscribedRegions.add(region);
        region.subscribe();
        Reverb.notifyAvailableRegionsUpdateListeners();
    }

    @Override
    public void unsubscribeFromRegion(Region region)
    {
        final Region r = region;
        if(region != null) {
            UnfollowRegionDto unfollowRegionDto;
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject jsonObject)
                {
                    unsubscribeToRegionCallback(r);
                }
            };
            try {
                unfollowRegionDto = new UnfollowRegionDto(Reverb.getInstance().getCurrentUserId(), region.getRegionId());
                com.re.reverb.network.RegionManagerImpl.unfollowRegion(listener, unfollowRegionDto);
            }
            catch (NotSignedInException e) {
                e.printStackTrace();
            }
        }
    }

    private void unsubscribeToRegionCallback(Region region) {
        region.unsubscribe();
        this.subscribedRegions.remove(region);
        Reverb.notifyAvailableRegionsUpdateListeners();
    }

    @Override
    public void updateRegionLists()
    {
        com.re.reverb.network.RegionManagerImpl.getNearbyRegions(Reverb.getInstance().getCurrentLocation());
        try
        {
            GetSubscribedRegionsDto subscribedRegionsDto = new GetSubscribedRegionsDto(Reverb.getInstance().getCurrentUserId());
            com.re.reverb.network.RegionManagerImpl.getSubscribedRegions(subscribedRegionsDto);
        } catch (NotSignedInException e)
        {
            e.printStackTrace();
            Log.e("Reverb", "Tried to fetch subscribed regions when user was not logged in!");
        }

    }

    @Override
    public void onLocationChanged(Location newLocation)
    {
        updateRegionLists();
//        this.update();
        if(this.currentRegion == null)
        {
            this.currentRegion = new CommonsRegion(newLocation);
            this.nearbyRegions.add(this.currentRegion);
            this.subscribedRegions.add(this.currentRegion);
        }
        else
        {
            this.currentRegion.update();
            this.currentRegion.setReadPermission(this.currentRegion.containsPoint(Reverb.getInstance().getCurrentLocation()) || this.subscribedRegions.contains(this.currentRegion));
            this.currentRegion.setWritePermission(this.currentRegion.containsPoint(Reverb.getInstance().getCurrentLocation()));
        }
        Reverb.notifyAvailableRegionsUpdateListeners();
//        Log.d("Reverb", "Current region is at location"+newLocation.getLatitude()+", "+newLocation.getLongitude());
    }


}
