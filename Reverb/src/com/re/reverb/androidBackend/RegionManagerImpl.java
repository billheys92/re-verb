package com.re.reverb.androidBackend;

import android.util.Log;

import com.google.android.gms.maps.Projection;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.regions.CommonsRegion;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.androidBackend.regions.dto.CreateRegionDto;
import com.re.reverb.androidBackend.regions.dto.FollowRegionDto;

import java.util.ArrayList;
import java.util.List;

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
        this.currentRegion.update();
        this.currentRegion.setReadPermission(this.currentRegion.containsPoint(Reverb.getInstance().getCurrentLocation()) || this.subscribedRegions.contains(region));
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
        //TODO: check if the region is already in the list before adding to list, if it is just update it
        // TODO: persist the region on server

        if(region != null)
        {
            CreateRegionDto regionDto = buildRegionDto(region);
            if(com.re.reverb.network.RegionManagerImpl.submitNewRegion(regionDto))
            {
                this.nearbyRegions.add(region);
                Reverb.notifyAvailableRegionsUpdateListeners();
            }
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
        if(region != null) {
            FollowRegionDto followRegionDto;
            try {
                followRegionDto = new FollowRegionDto(Reverb.getInstance().getCurrentUserId(), region.getRegionId());
                this.subscribedRegions.add(region);
                region.subscribe();
                com.re.reverb.network.RegionManagerImpl.followRegion(followRegionDto);
                Reverb.notifyAvailableRegionsUpdateListeners();
            }
            catch (NotSignedInException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean unsubscribeFromRegion(Region region)
    {
        if(region != null) {
            region.unsubscribe();
            boolean success = this.subscribedRegions.remove(region);
            Reverb.notifyAvailableRegionsUpdateListeners();
            return success;
        }
        return false;
    }

    @Override
    public void updateRegionLists()
    {
        //TODO: re-fetch nearby and subscribed
    }

    @Override
    public void onLocationChanged(Location newLocation)
    {
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
