package com.re.reverb.androidBackend.regions.dto;

/**
 * Created by Bill on 2015-03-02.
 */
public class UnfollowRegionDto
{
    public int Poster_id;
    public int Region_id;

    public UnfollowRegionDto(int userId,
                           int regionId)
    {
        this.Poster_id = userId;
        this.Region_id = regionId;
    }
}
