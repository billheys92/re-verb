package com.re.reverb.androidBackend;

import java.util.LinkedList;

import com.re.reverb.androidBackend.errorHandling.EmptyPostException;

public class TestMain
{
	static DummyFeed feed = new DummyFeed();
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		LinkedList<Post> posts = (LinkedList<Post>) feed.getAllPosts();
		for (Post p : posts)
		{
			try
			{
				System.out.println(p.getPostContent().getPostData());
			} catch (EmptyPostException e)
			{
				e.printStackTrace();
			}
		}
		
		System.out.println("");
		testIncrement();
		System.out.println("");
		testIncrement();
		System.out.println("");
		testIncrement();
		
	}
	
	private static void testIncrement(){
		feed.incrementFeed();
		LinkedList<Post> posts = (LinkedList<Post>) feed.getAllPosts();
		for (Post p : posts)
		{
			try
			{
				System.out.println(p.getPostContent().getPostData());
			} catch (EmptyPostException e)
			{
				e.printStackTrace();
			}
		}
	}

}
