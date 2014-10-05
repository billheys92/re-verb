package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.IncompletePostException;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;

public class PostFactory
{

    public static Post createPost(String text, boolean anonymous) throws NotSignedInException{
        TextPostContent content = new TextPostContent(text);
        Post post = new Post();
        post.setPostContent(content);
        UserProfile user = Reverb.getInstance().getCurrentUser();
        if(user == null) throw new NotSignedInException();
        post.setUserId(user.getUserId());
        post.setAnonymous(anonymous);


        return post;
    };
	

}
