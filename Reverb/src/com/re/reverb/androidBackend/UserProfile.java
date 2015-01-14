package com.re.reverb.androidBackend;

import java.util.UUID;

/**
 * Created by Bill on 2014-09-12.
 */
public class UserProfile {

    private String email;
    private String name;
    private String nickname;
    private String description;
    private int id;

    public UserProfile(String email, String name, String nickname, String description, int id) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.description = description;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
}
