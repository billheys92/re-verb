package com.re.reverb.androidBackend.account;

/**
 * Created by christopherhowse on 15-03-08.
 */
public class UpdateHandleDto
{
    public final int Poster_id;
    public final String Handle;

    public UpdateHandleDto(int poster_id,
                           String handle)
    {
        Poster_id = poster_id;
        Handle = handle;
    }
}
