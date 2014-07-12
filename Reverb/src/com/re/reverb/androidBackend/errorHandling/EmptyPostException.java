package com.re.reverb.androidBackend.errorHandling;

public class EmptyPostException extends Exception
{
	
	public EmptyPostException(){
		super("Post is missing it's data");
	}

}
