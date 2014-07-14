package feed;

import android.graphics.Bitmap;

//CHILD
public class MessageReply
{
	private Bitmap image;
	private String userName;
	private String handle;
	private String body;

	public MessageReply()
	{
		
	}
	
	public MessageReply(String string)
	{
		this.setBody(string);
	}
	
	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getHandle()
	{
		return handle;
	}

	public void setHandle(String handle)
	{
		this.handle = handle;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

}