package com.re.reverb.androidBackend.account;

public class UserProfile {

    private String email;
    private String username;
    private String handle;
    private String description;
    private String token;
    private int id;

    public UserProfile(String email, String username, String handle, String description, String token, int id) {
        this.email = email;
        this.username = username;
        this.handle = handle;
        this.description = description;
        this.token = token;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return id;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}
