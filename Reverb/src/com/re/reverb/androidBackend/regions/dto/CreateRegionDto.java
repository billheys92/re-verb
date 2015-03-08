package com.re.reverb.androidBackend.regions.dto;

import android.graphics.Rect;

import com.re.reverb.androidBackend.regions.CircleRegionShape;
import com.re.reverb.androidBackend.regions.RectangleRegionShape;
import com.re.reverb.androidBackend.regions.RegionShape;

import java.util.ArrayList;
import java.util.List;

public class CreateRegionDto
{
    public int Poster_id;
    public String Name;
    public String Description;
    public ArrayList<RegionCircleDto> Circles = new ArrayList<RegionCircleDto>();
    public ArrayList<RegionRectangleDto> Rectangles = new ArrayList<RegionRectangleDto>();
    public String Picture_name;

    public CreateRegionDto(int userId,
                         String name,
                         String description,
                         List<RegionShape> regionShapes)
    {
        this.Poster_id = userId;
        this.Name = name;
        this.Description = description;
        for(RegionShape shape: regionShapes) {
            if(shape instanceof CircleRegionShape) {
                Circles.add(new RegionCircleDto((CircleRegionShape)shape));
            }
            else if(shape instanceof RectangleRegionShape) {
                Rectangles.add(new RegionRectangleDto((RectangleRegionShape)shape));
            }
        }
    }
}
