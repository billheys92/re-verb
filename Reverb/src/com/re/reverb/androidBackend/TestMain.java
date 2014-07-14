package com.re.reverb.androidBackend;

import java.util.LinkedList;
import java.util.Stack;

import com.re.reverb.androidBackend.errorHandling.EmptyPostException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;

public class TestMain
{
	static DummyFeed feed = new DummyFeed();
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Stack<Post> posts = new Stack<Post>();
		try
		{
			posts = (Stack<Post>) feed.getAllPosts();
		} catch (UnsuccessfulRefreshException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		try
		{
			feed.incrementFeed();
		} catch (UnsuccessfulFeedIncrementException e2)
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Stack<Post> posts = new Stack<Post>();
		try
		{
			posts = (Stack<Post>) feed.getAllPosts();
		} catch (UnsuccessfulRefreshException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
