package com.re.reverb.androidBackend.post.dto;

import com.re.reverb.androidBackend.post.content.dto.PostContentDto;

public class CreatePostDto
{
    public int Poster_id;
    public boolean Anon_flag;
    public double Location_lat;
    public double Location_long;
    public String Message_body;

    public CreatePostDto(int userId,
                         boolean anonymous,
                         double latitude,
                         double longitude,
                         String content)
    {
        this.Poster_id = userId;
        this.Anon_flag = anonymous;
        this.Location_lat = latitude;
        this.Location_long = longitude;
        this.Message_body = content;
    }
}

