package com.re.reverb.androidBackend;

public class SimplePostFactory implements PostFactory
{

	@Override
	public Post createPost(int userId, String text)
	{
		TextPostContent content = new TextPostContent(text);
		Post post = new Post(userId);
        post.setPostContent(content);
		return post;
	}

}
