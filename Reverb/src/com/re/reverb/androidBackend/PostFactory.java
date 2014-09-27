package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.post.Post;

import java.util.UUID;

public interface PostFactory
{
	
	public Post createPost(int userId, String text);
	//public Post createPost(Image image);
	//public Post createPost(Video video);	
	//public Post createPost(Audio audio);
	

}
