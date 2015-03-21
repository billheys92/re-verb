package com.re.reverb.network;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.androidBackend.regions.RegionFactory;
import com.re.reverb.androidBackend.regions.dto.ReceiveRegionSummaryDto;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Bill on 2015-03-20.
 */
public class UpdateManagerImpl extends PersistenceManagerImpl
{
    public static ArrayList<String> getNewUpdates(String lastUpdateTime) throws NotSignedInException
    {
        String params = String.format("?commandtype=get&command=getNotifications&user=%s&lastupdate=%s",Reverb.getInstance().getCurrentUserId(),lastUpdateTime);
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
        return null;    //TODO: not this
    }
}
