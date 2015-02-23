package com.re.reverb.androidBackend.account;

public class CreateUserDto
{
    public String username;
    public String handle;
    public String email;
    public String token;

    public CreateUserDto(String username, String handle, String email, String token)
    {
        this.username = username;
        this.handle = handle;
        this.email = email;
        this.token = token;
    }
}
