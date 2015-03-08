package com.re.reverb.androidBackend.account;

public class UpdateUsernameDto
{
    public final int Poster_id;
    public final String Name;

    public UpdateUsernameDto(int poster_id,
                             String name)
    {
        Poster_id = poster_id;
        Name = name;
    }
}
