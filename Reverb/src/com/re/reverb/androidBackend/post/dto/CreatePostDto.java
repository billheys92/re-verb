package com.re.reverb.androidBackend.post.dto;

public class CreatePostDto
{
    public int Poster_id;
    public boolean Anon_flag;
    public double Location_lat;
    public double Location_long;
    public int Region_id;
    public String Message_body;
    public String Picture_name;

    public CreatePostDto(int userId,
                         boolean anonymous,
                         double latitude,
                         double longitude,
                         int regionId,
                         String content)
    {
        this.Poster_id = userId;
        this.Anon_flag = anonymous;
        this.Location_lat = latitude;
        this.Location_long = longitude;
        this.Region_id = regionId;
        this.Message_body = content;
        this.Picture_name = null;
    }
}

