package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.post.Post;

import java.util.Collection;

/**
 * Created by Bill on 2014-09-12.
 */
public interface PersistenceManager {

    public Collection<Post> getPosts(float latitude, float longitude);
    public Collection<Post> getPosts(Region region);
    public boolean submitPostToRegion(Post post, Region region);
    public UserProfile getUserProfileFromLogin(String email, String password);

    public void saveUserProfile(UserProfile user);
    public boolean addNewUserProfile(UserProfile user);


}
