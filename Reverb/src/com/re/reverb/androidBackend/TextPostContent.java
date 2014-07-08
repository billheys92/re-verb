package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.EmptyPostException;
import com.re.reverb.androidBackend.errorHandling.InvalidPostDataTypeException;

public class TextPostContent implements PostContent
{
	
	String postText;
	
	public TextPostContent(String text)
	{
		this.postText = text;
	}

	@Override
	public Object getPostData() throws EmptyPostException
	{
		if(postText == null || postText.length() == 0)
		{
			throw new EmptyPostException();
		}
		//TODO: create a textbox fragment to return
		return null;
	}
	
	public void setPostData(Object o) throws InvalidPostDataTypeException{
		if(!(o instanceof String))
		{
			throw new InvalidPostDataTypeException(postText.getClass().getName(), o.getClass().getName());
		}
	}

}
