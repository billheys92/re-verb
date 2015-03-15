package com.re.reverb.androidBackend.regions.dto;

/**
 * Created by Bill on 2015-02-21.
 */
public class GetSubscribedRegionsDto
{

    private int Poster_id;

    public GetSubscribedRegionsDto(int currentUserId)
    {
        this.Poster_id = currentUserId;
    }

    public int getPoster_id()
    {
        return Poster_id;
    }
}
