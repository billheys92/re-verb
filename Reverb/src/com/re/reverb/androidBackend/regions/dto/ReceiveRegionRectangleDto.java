package com.re.reverb.androidBackend.regions.dto;

/**
 * Created by Bill on 2015-03-03.
 */
public class ReceiveRegionRectangleDto
{
    private String rect;

    public ReceiveRegionRectangleDto(String rectString) {
        this.rect = rectString;
    }

    public String getRect()
    {
        return rect;
    }

}
