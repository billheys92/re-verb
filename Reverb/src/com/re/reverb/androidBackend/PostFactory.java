package com.re.reverb.androidBackend;

import java.util.UUID;

public interface PostFactory
{
	
	public Post createPost(UUID userId, String text);
	//public Post createPost(Image image);
	//public Post createPost(Video video);	
	//public Post createPost(Audio audio);
	

}
