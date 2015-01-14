package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.EmptyPostException;
import com.re.reverb.androidBackend.errorHandling.InvalidPostDataTypeException;

public interface PostContent
{
	
	public Object getPostData() throws EmptyPostException;
	
	public void setPostData(Object o) throws InvalidPostDataTypeException;

    public boolean isEmpty();

    public String getMessageString();

}
