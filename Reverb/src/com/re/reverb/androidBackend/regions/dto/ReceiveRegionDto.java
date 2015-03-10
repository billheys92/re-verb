package com.re.reverb.androidBackend.regions.dto;

import com.google.gson.reflect.TypeToken;
import com.re.reverb.androidBackend.post.Post;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Bill on 2015-03-02.
 */
public class ReceiveRegionDto
{
    int Region_id;
    String Region_name;
    String Description;
    String Create_time;
    String Update_time;
    String Picture_name;
    ArrayList<ReceiveRegionCircleDto> circles;
    ArrayList<ReceiveRegionRectangleDto> rectangles;

    public ReceiveRegionDto(int Region_id,
                   String Region_name,
                   String Description,
                   String Create_time,
                   String Update_time,
                   String Picture_name)
    {
        this.Region_id = Region_id;
        this.Region_name = Region_name;
        this.Description = Description;
        this.Create_time = Create_time;
        this.Update_time = Update_time;
        this.Picture_name = Picture_name;
    }

    public void setCirclesDto(ArrayList<ReceiveRegionCircleDto> circlesDto)
    {
        this.circles = circlesDto;
    }

    public void setRectanglesDto(ArrayList<ReceiveRegionRectangleDto> rectanglesDto)
    {
        this.rectangles = rectanglesDto;
    }

    public int getRegion_id()
    {
        return Region_id;
    }

    public String getRegion_name()
    {
        return Region_name;
    }

    public String getDescription()
    {
        return Description;
    }

    public String getCreate_time()
    {
        return Create_time;
    }

    public String getUpdate_time()
    {
        return Update_time;
    }

    public String getPicture_name()
    {
        return Picture_name;
    }

    public ArrayList<ReceiveRegionCircleDto> getCircles()
    {
        return circles;
    }

    public ArrayList<ReceiveRegionRectangleDto> getRectangles()
    {
        return rectangles;
    }

}
