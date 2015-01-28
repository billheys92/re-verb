package com.re.reverb.androidBackend;

import android.util.Log;

import com.google.android.gms.maps.Projection;
import com.re.reverb.androidBackend.regions.CommonsRegion;
import com.re.reverb.androidBackend.regions.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bill on 2015-01-11.
 */
public class RegionManagerImpl implements RegionManager, LocationUpdateListener
{
    private Region currentRegion;
    private List<Region> nearbyRegions;
    private List<Region> subscribedRegions;

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
    public List<Region> getNearbyRegions()
    {
        return this.nearbyRegions;
    }

    @Override
    public List<Region> getSubscribedRegions()
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
    public void addRegion(Region region)
    {
        //TODO: check if the region is already in the list before adding to list, if it is just update it
        // TODO: persist the region on server

        this.nearbyRegions.add(region);
        this.subscribedRegions.add(region);
        Reverb.notifyAvailableRegionsUpdateListeners();
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
