package com.re.reverb.androidBackend.account;

public class CreateUserDto
{
    public String Name;
    public String Handle;
    public String Email;
    public String Token;
    public String About_me;
    public String Profile_picture;

    public CreateUserDto(String Name, String Handle, String Email, String Token, String About_me)
    {
        this.Name = Name;
        this.Handle = Handle;
        this.Email = Email;
        this.Token = Token;
        this.About_me = About_me;
        this.Profile_picture = null;
    }
}
