package com.re.reverb.androidBackend.account;

/**
 * Created by Colin on 3/13/2015.
 */
public class UpdateProfilePictureDto
{
    public int Poster_id;
    public String Profile_picture;

    public UpdateProfilePictureDto(int poster_id,
                                   String profile_picture)
    {
        Poster_id = poster_id;
        Profile_picture = profile_picture;
    }
}
