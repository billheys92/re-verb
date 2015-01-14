package com.re.reverb.androidBackend.post.dto;

import com.re.reverb.androidBackend.post.content.dto.PostContentDto;

public class CreatePostDto
{
    public int userId;
    public boolean anonymous;
    public double latitude;
    public double longitude;
    public PostContentDto content;

    public CreatePostDto(int userId,
                         boolean anonymous,
                         double latitude,
                         double longitude,
                         PostContentDto content)
    {
        this.userId = userId;
        this.anonymous = anonymous;
        this.latitude = latitude;
        this.longitude = longitude;
        this.content = content;
    }
}
