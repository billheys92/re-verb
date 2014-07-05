package com.re.reverb.androidBackend;

import java.util.UUID;

public class SimplePostFactory implements PostFactory
{

	@Override
	public Post createPost(UUID userId, String text)
	{
		TextPostContent content = new TextPostContent(text);
		Post post = new Post(userId,content);
		return post;
	}

}
