package com.re.reverb.androidBackend.regions;

import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.regions.dto.ReceiveRegionCircleDto;
import com.re.reverb.androidBackend.regions.dto.ReceiveRegionDto;
import com.re.reverb.androidBackend.regions.dto.ReceiveRegionRectangleDto;
import com.re.reverb.androidBackend.regions.dto.ReceiveRegionSummaryDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bill on 2015-03-02.
 */
public class RegionFactory
{

    public static Region createRegionFromSummary(ReceiveRegionSummaryDto dto)
    {
        Region r = new Region();
        r.setRegionId(dto.getRegion_id());
        r.setName(dto.getRegion_name());
        r.setDescription(dto.getDescription());
        Date createTime = null;
        Date updateTime = null;
        try {
            if(dto.getCreate_time() != null)
            {
                createTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(dto.getCreate_time());
            }
            if(dto.getUpdate_time() != null)
            {
                createTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(dto.getUpdate_time());
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        r.setCreationTime(createTime);
        r.setCreationTime(updateTime);
        r.setShapes(null);
        return r;
    }

    public static Region createRegionFromDto(ReceiveRegionDto dto)
    {
        if(dto.getCircles() == null || dto.getRectangles() == null || (dto.getCircles().size() == 0 && dto.getRectangles().size() == 0))
        {
            return null;
        }
        else
        {
            Region r = new Region();

            r.setRegionId(dto.getRegion_id());
            r.setName(dto.getRegion_name());
            r.setDescription(dto.getDescription());
            Date createTime = null;
            Date updateTime = null;
            try {
                if(dto.getCreate_time() != null)
                {
                    createTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(dto.getCreate_time());
                }
                if(dto.getUpdate_time() != null)
                {
                    createTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(dto.getUpdate_time());
                }
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            r.setCreationTime(createTime);
            r.setCreationTime(updateTime);
            ArrayList<CircleRegionShape> circleRegionShapes = getCirclesFromDto(dto);
            ArrayList<RectangleRegionShape> rectangleRegionShapes = getRectanglesFromDto(dto);
            ArrayList<RegionShape> shapes = new ArrayList<RegionShape>();
            shapes.addAll(circleRegionShapes);
            shapes.addAll(rectangleRegionShapes);
            r.setShapes(shapes);
            return r;
        }
    }

    private static ArrayList<RectangleRegionShape> getRectanglesFromDto(ReceiveRegionDto dto)
    {
        ArrayList<RectangleRegionShape> rectangles = new ArrayList<RectangleRegionShape>();
        for(ReceiveRegionRectangleDto dtoRect: dto.getRectangles())
        {
            String polygon = dtoRect.getRect();
            polygon = polygon.replace("POLYGON((","");
            polygon = polygon.replace(")","");
            String[] coordinates = polygon.split(",");
            ArrayList<Location> points = new ArrayList<Location>(4);
            for(int i = 0; i < 4; i++)
            {
                String coord = coordinates[i];
                String [] latlng = coord.split(" ");
                points.add(i,new Location(Float.parseFloat(latlng[0]),Float.parseFloat(latlng[1])));
            }
            RectangleRegionShape rectangle = new RectangleRegionShape(points.get(0),points.get(1),points.get(2),points.get(3));

            rectangles.add(rectangle);
        }
        return rectangles;

    }

    private static ArrayList<CircleRegionShape> getCirclesFromDto(ReceiveRegionDto dto)
    {

        ArrayList<CircleRegionShape> circles = new ArrayList<CircleRegionShape>();
        for(ReceiveRegionCircleDto dtoCircle: dto.getCircles())
        {
            double cLat = dtoCircle.getLat();
            double cLong = dtoCircle.getLon();
            double rad = dtoCircle.getRadius();
            CircleRegionShape c = new CircleRegionShape(new Location(cLat,cLong),rad);
            circles.add(c);
        }
        return circles;
    }
}
