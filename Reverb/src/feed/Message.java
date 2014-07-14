package feed;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

//PARENT
public class Message
{
	private Bitmap image;
	private String userName;
	private String handle;
	private String body;
	private Integer favorites;
	private ArrayList<MessageReply> children;

	
	public Message()
	{
		int faveNum = (int) (Math.random()*10);
		this.setFavorites(faveNum);
	}
	
	public Message(String string)
	{
		this.setBody(string);
		int faveNum = (int) (Math.random()*10);
		this.setFavorites(faveNum);
		//this.image = new Bitmap()
		
	}

	public ArrayList<MessageReply> getChildren()
	{
		return children;
	}

	public void setChildren(ArrayList<MessageReply> children)
	{
		this.children = children;
	}
	
	public void addChild(MessageReply child)
	{
		this.children.add(child);
	}

	public Integer getFavorites()
	{
		return favorites;
	}

	public void setFavorites(Integer favorites)
	{
		this.favorites = favorites;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getHandle()
	{
		return handle;
	}

	public void setHandle(String handle)
	{
		this.handle = handle;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}
}
