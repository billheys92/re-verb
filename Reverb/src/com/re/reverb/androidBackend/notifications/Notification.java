package com.re.reverb.androidBackend.notifications;

/**
 * Created by Bill on 2015-03-20.
 */
public class Notification
{
    private String notificationBody;
    private Integer regionId;
    private Integer messageId;

    public String getNotificationBody()
    {
        return notificationBody;
    }

    public void setNotificationBody(String notificationBody)
    {
        this.notificationBody = notificationBody;
    }

    public Integer getRegionId()
    {
        return regionId;
    }

    public void setRegionId(Integer regionId)
    {
        this.regionId = regionId;
    }

    public Integer getMessageId()
    {
        return messageId;
    }

    public void setMessageId(Integer messageId)
    {
        this.messageId = messageId;
    }

}
