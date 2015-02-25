package com.re.reverb.androidBackend.post.dto;

public class CreateReplyPostDto
{
    public int Poster_id;
    public int Message_id; //message id of the post being replied to
    public boolean Anon_flag;
    public double Location_lat;
    public double Location_long;
    public String Message_body;

    public CreateReplyPostDto(int userId,
                         int messageId,
                         boolean anonymous,
                         double latitude,
                         double longitude,
                         String content)
    {
        this.Poster_id = userId;
        this.Message_id = messageId;
        this.Anon_flag = anonymous;
        this.Location_lat = latitude;
        this.Location_long = longitude;
        this.Message_body = content;
    }
}
