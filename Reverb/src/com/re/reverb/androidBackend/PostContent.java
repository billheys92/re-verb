package com.re.reverb.androidBackend;

import com.re.reverb.androidBackend.errorHandling.EmptyPostException;
import com.re.reverb.androidBackend.errorHandling.InvalidPostDataTypeException;

import android.support.v4.app.Fragment;

public interface PostContent
{
	
	public Object getPostData() throws EmptyPostException;
	
	public void setPostData(Object o) throws InvalidPostDataTypeException;

}
