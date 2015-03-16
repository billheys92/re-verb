package com.re.reverb.androidBackend.account;

public class UserProfile {

    public String Email_address;
    public String Name;
    public String Handle;
    public String About_me;
    public String Token;
    public int User_id;
    public final String Profile_picture;

    public UserProfile(String email,
                       String name,
                       String handle,
                       String about_me,
                       String token,
                       int user_id) {
        Email_address = email;
        Name = name;
        Handle = handle;
        About_me = about_me;
        Token = token;
        User_id = user_id;
        Profile_picture = null;
    }

    public String getEmail_address()
    {
        return Email_address;
    }

    public String getName()
    {
        return Name;
    }

    public String getHandle()
    {
        return Handle;
    }

    public String getAbout_me()
    {
        return About_me;
    }
}
