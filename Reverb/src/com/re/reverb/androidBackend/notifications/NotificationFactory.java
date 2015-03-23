package com.re.reverb.androidBackend.notifications;

import java.util.ArrayList;

/**
 * Created by Bill on 2015-03-23.
 */
public class NotificationFactory
{

    public static Notification buildNotification(ArrayList<Notification> notificationArrayList)
    {
        if(notificationArrayList.size() == 1)
        {
            return notificationArrayList.get(0);
        }
        else if (notificationArrayList.size() > 1)
        {
            Notification notification = new Notification();
            notification.setMessageId(null);
            notification.setRegionId(null);
            notification.setNotificationBody("Users have replied to your posts!");
            return notification;
        }
        else return null;
    }

    public static Notification buildNotification(NotificationDto dto)
    {
        Notification notification = new Notification();
        notification.setMessageId(new Integer(dto.getMessage_id()));
        notification.setNotificationBody(dto.getNotification_body());
        notification.setRegionId(new Integer(dto.getRegion_id()));
        return notification;
    }
}
