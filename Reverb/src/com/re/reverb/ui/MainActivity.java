package com.re.reverb.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.re.reverb.R;

public class MainActivity extends FragmentActivity
{
	
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        FragmentManager fragManager = getSupportFragmentManager();
        
        Fragment fragment = fragManager.findFragmentById(R.id.fragmentContainer);
        if(fragment == null){
        	fragment = new MainFeedFragment();
        	fragManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		System.out.println("Touched");
		return true;
	}
	
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
	    System.out.println("Message Sent");
	}


}
