package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.regions.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bill on 2015-01-11.
 */
public interface RegionManager
{

    public void setCurrentRegion(Region region);

    public Region getCurrentRegion();

    public ArrayList<Region> getNearbyRegions();

    public ArrayList<Region> getSubscribedRegions();

    public void setNearbyRegions(ArrayList<Region> regions);

    public void setSubscribedRegions(ArrayList<Region> regions);

    public void updateRegionLists();

    public ArrayList<String> getNearbyRegionNames();

    public ArrayList<String> getSubscribedRegionNames();

    public void createNewRegion(Region region);

    public void subscribeToRegion(Region region);

    public boolean isRegionSubscribed(int regionId);

    public boolean insideRegion(int regionId);

    /**
     *
     * @param region
     * @return true if region unsubscribed from, false if it was never subscribed to
     */
    public void unsubscribeFromRegion(Region region);

}
