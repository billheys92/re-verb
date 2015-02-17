package com.re.reverb.androidBackend.regions.dto;
/**
 * Created by Bill on 2015-02-17.
 */
public class FollowRegionDto
{
    public int Poster_id;
    public int Region_id;

    public FollowRegionDto(int userId,
                           int regionId)
    {
        this.Poster_id = userId;
        this.Region_id = regionId;
    }
}
