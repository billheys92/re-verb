package com.re.reverb.androidBackend.regions.dto;

import com.re.reverb.androidBackend.post.Post;

import java.text.SimpleDateFormat;

/**
 * Created by Bill on 2015-03-02.
 */
public class ReceiveRegionSummaryDto
{
    int Region_id;
    String Region_name;
    String Description;
    String Create_time;
    String Update_time;
    String Picture_name;
    int Num_members;
    int Num_posts;

    public ReceiveRegionSummaryDto(int Region_id,
                            String Region_name,
                            String Description,
                            String Create_time,
                            String Update_time,
                            String Picture_name,
                            int Num_members,
                            int Num_posts)
    {
        this.Region_id = Region_id;
        this.Region_name = Region_name;
        this.Description = Description;
        this.Create_time = Create_time;
        this.Update_time = Update_time;
        this.Picture_name = Picture_name;
        this.Num_members = Num_members;
        this.Num_posts = Num_posts;
    }

    public int getRegion_id() {
        return Region_id;
    }

    public String getCreate_time() {
        return Create_time;
    }

    public String getUpdate_time() {
        return Update_time;
    }

    public String getDescription() {
        return Description;
    }

    public String getRegion_name() {
        return Region_name;
    }

    public String getPicture_name()
    {
        return Picture_name;
    }
    public int getNum_members() {
        return Num_members;
    }

    public int getNum_posts()
    {
        return Num_posts;
    }
}
