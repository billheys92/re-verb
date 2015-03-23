package com.re.reverb.androidBackend.notifications;

/**
 * Created by Bill on 2015-03-20.
 */
public class NotificationDto
{

    String Notification_body;
    int User_id;
    int Region_id;
    int Message_id;

    public NotificationDto(
            String Notification_body,
            int User_id,
            int Region_id,
            int Message_id
    )
    {
        this.Notification_body = Notification_body;
        this.User_id = User_id;
        this.Region_id = Region_id;
        this.Message_id = Message_id;
    }

    public String getNotification_body()
    {
        return Notification_body;
    }

    public void setNotification_body(String notification_body)
    {
        Notification_body = notification_body;
    }

    public int getUser_id()
    {
        return User_id;
    }

    public void setUser_id(int user_id)
    {
        User_id = user_id;
    }

    public int getRegion_id()
    {
        return Region_id;
    }

    public void setRegion_id(int region_id)
    {
        Region_id = region_id;
    }

    public int getMessage_id()
    {
        return Message_id;
    }

    public void setMessage_id(int message_id)
    {
        Message_id = message_id;
    }


}
