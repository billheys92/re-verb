package com.re.reverb.androidBackend.post.dto;

/**
 * Created by christopherhowse on 15-03-04.
 */
public class PostActionResponseDto
{
    public int increment;
    public int db_response;

    public PostActionResponseDto(int increment, int db_response)
    {
        this.increment = increment;
        this.db_response = db_response;
    }
}
