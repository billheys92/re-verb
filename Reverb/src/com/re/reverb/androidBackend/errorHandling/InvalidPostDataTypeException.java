package com.re.reverb.androidBackend.errorHandling;

public class InvalidPostDataTypeException extends Exception
{
	
	public InvalidPostDataTypeException(String expectedDataType, String invalidDataType){
		super("Post was expecting "+expectedDataType+" data, got "+invalidDataType+" data");
	}

}
