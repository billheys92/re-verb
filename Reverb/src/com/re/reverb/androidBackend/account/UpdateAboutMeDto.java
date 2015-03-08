package com.re.reverb.androidBackend.account;

/**
 * Created by christopherhowse on 15-03-08.
 */
public class UpdateAboutMeDto
{
    public final int Poster_id;
    public final String About_me;

    public UpdateAboutMeDto(int poster_id,
                            String about_me)
    {
        Poster_id = poster_id;
        About_me = about_me;
    }
}
