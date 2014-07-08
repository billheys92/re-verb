package com.re.reverb.ui;

import java.util.ArrayList;
import java.util.List;

public class Message
{
	public String string;
	public final List<String> children = new ArrayList<String>();
	
	public Message(String string)
	{
		this.string = string;
	}
}
