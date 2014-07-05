package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.re.reverb.androidBackend.TextPostContent;
import com.re.reverb.androidBackend.errorHandling.EmptyPostException;
import com.re.reverb.androidBackend.errorHandling.InvalidPostDataTypeException;

public class TextPostContentTest
{

	@Test
	public void testGetUnsetDataPost(){
		TextPostContent t = new TextPostContent("");
		boolean thrown = false;
		try
		{
			t.getPostData();
		} catch (EmptyPostException e)
		{
			thrown = true;
		}
		assertTrue(thrown);
	}
	
	@Test
	public void testGetAndSetDataPost(){
		TextPostContent t = new TextPostContent("");
		try
		{
			t.setPostData("I love re:verb so much!");
		} catch (InvalidPostDataTypeException e1)
		{
			
			assertTrue("This shouldn't have thrown an exception!",false);
			e1.printStackTrace();
		}
		String p = "";
		try
		{
			p = (String) t.getPostData();
		} catch (EmptyPostException e)
		{
			assertTrue("This shouldn't have thrown an exception!",false);
			e.printStackTrace();
		}
		assertEquals("This shouldn't have thrown an exception!",p);
	}
	
	@Test
	public void testSetInvalidDataPost(){
		TextPostContent t = new TextPostContent("");
		boolean thrown = false;
		try
		{
			t.setPostData(new Integer(1));
		} catch (InvalidPostDataTypeException e)
		{
			thrown = true;
		}
		assertTrue(thrown);
	}

}
