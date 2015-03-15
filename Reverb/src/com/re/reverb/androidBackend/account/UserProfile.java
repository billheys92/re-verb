package com.re.reverb.androidBackend.account;

public class UserProfile {

    public final String Email_address;
    public final String Name;
    public final String Handle;
    public final String About_me;
    public String Token;
    public final int User_id;
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
}
