package com.re.reverb.androidBackend.post.dto;

public class PostActionDto
{
    public final int Message_id;
    public final int Poster_id;

    public PostActionDto(int message_id,
                           int poster_id)
    {
        Message_id = message_id;
        Poster_id = poster_id;
    }
}
