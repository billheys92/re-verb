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
		return postText;
	}
	
	public void setPostData(Object o) throws InvalidPostDataTypeException{
		if(!(o instanceof String))
		{
			throw new InvalidPostDataTypeException(postText.getClass().getName(), o.getClass().getName());
		}
	}

    @Override
    public boolean isEmpty() {
        if(this.postText == null || this.postText.replaceAll("\\s+","").length() == 0)
        {
            return false;
        }
        return true;
    }

    @Override
    public String getMessageString() {
        try {
            return (String)getPostData();
        } catch (EmptyPostException e) {
            e.printStackTrace();
            return null;
        }
    }

}
