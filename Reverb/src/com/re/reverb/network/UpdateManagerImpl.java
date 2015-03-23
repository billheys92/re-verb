package com.re.reverb.network;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.notifications.Notification;
import com.re.reverb.androidBackend.notifications.NotificationDto;
import com.re.reverb.androidBackend.notifications.NotificationFactory;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.androidBackend.regions.RegionFactory;
import com.re.reverb.androidBackend.regions.dto.ReceiveRegionSummaryDto;
import com.re.reverb.ui.BackgroundUpdateService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Bill on 2015-03-20.
 */
public class UpdateManagerImpl extends PersistenceManagerImpl
{
    public static ArrayList<String> getNewUpdates(String lastUpdateTime, final BackgroundUpdateService service) throws NotSignedInException
    {
        String params = String.format("?commandtype=get&command=getNotifications&user=%s&lastupdate=%s",Reverb.getInstance().getCurrentUserId(),lastUpdateTime);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                System.out.println("Response is: " + response.toString());
                ArrayList<Notification> returnedNotifications = new ArrayList<Notification>();
                for(int i = 0; i < response.length(); i++){
                    try {
                        Gson gson = new Gson();
                        NotificationDto notificationDto = gson.fromJson(response.get(i).toString(), NotificationDto.class);
                        returnedNotifications.add(NotificationFactory.buildNotification(notificationDto));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Notification notification = NotificationFactory.buildNotification(returnedNotifications);
                if(notification != null)
                {
                    service.onNotificationsReceived(notification);
                }
                else
                {
                    service.onNoNotificationsReceived();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                service.onNoNotificationsReceived();
            }
        };
        requestJsonArray(listener, errorListener, baseURL + params);
        return null;    //TODO: not this
    }
}
