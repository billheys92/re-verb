package test;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import junit.framework.TestCase;

import org.junit.Test;

import com.re.reverb.androidBackend.DummyFeed;
import com.re.reverb.androidBackend.Post;
import com.re.reverb.androidBackend.errorHandling.EmptyPostException;

public class DummyFeedTest extends TestCase
{

	@Test
	public void testRefresh()
	{
		DummyFeed feed = new DummyFeed();
		LinkedList<Post> posts = (LinkedList<Post>) feed.getAllPosts();
		for (Post p : posts)
		{
			try
			{
				System.out.println(p.getPostContent().getPostData());
			} catch (EmptyPostException e)
			{
				assertTrue(false);
				e.printStackTrace();
			}
		}
	}

}
