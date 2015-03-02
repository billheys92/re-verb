package com.re.reverb.androidBackend.regions;

import com.re.reverb.androidBackend.regions.dto.ReceiveRegionSummaryDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
}
