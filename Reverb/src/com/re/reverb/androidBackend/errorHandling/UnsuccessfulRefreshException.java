package com.re.reverb.androidBackend.errorHandling;

public class UnsuccessfulRefreshException extends Exception
{

	public UnsuccessfulRefreshException(String refreshNumber){
		super("Refresh #"+refreshNumber+" was unsuccessful");
	}

}
