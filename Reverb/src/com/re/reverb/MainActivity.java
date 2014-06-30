package com.re.reverb;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MainActivity extends FragmentActivity
{
	
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
	    
	    ////////Network Stuff Starts////////////
        //String stringUrl = urlText.getText().toString();
	    String stringUrl = "http://192.168.2.18:8888/MAMP/helloworld.php"; //location of file on server
        ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) 
        {
        	new DownloadWebpageTask().execute(stringUrl);
        } 
        else 
        {
            //textView.setText("No network connection available.");
        	System.out.println("No network connection available.");
        }
	}
	
	private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
              
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
        	EditText ed = (EditText)findViewById(R.id.edit_message);
        	ed.setHint(result);
        	System.out.println(result);
       }
     // Given a URL, establishes an HttpUrlConnection and retrieves
     // the web page content as a InputStream, which it returns as
     // a string.
     private String downloadUrl(String myurl) throws IOException {
         InputStream is = null;
         // Only display the first 500 characters of the retrieved
         // web page content.
         int len = 500;
             
         try {
        	 System.out.println("trying url");
             URL url = new URL(myurl);
             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
             conn.setReadTimeout(10000 /* milliseconds */);
             conn.setConnectTimeout(15000 /* milliseconds */);
             conn.setRequestMethod("GET");
             conn.setDoInput(true);
             // Starts the query
             conn.connect();
             int response = conn.getResponseCode();
             //Log.d(DEBUG_TAG, "The response is: " + response);
             System.out.println("The response is: " + response);
             is = conn.getInputStream();

             // Convert the InputStream into a string
             String contentAsString = readIt(is, len);
             return contentAsString;
             
         // Makes sure that the InputStream is closed after the app is
         // finished using it.
         } finally {
             if (is != null) {
                 is.close();
             } 
         }
     }
     // Reads an InputStream and converts it to a String.
     public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
         Reader reader = null;
         reader = new InputStreamReader(stream, "UTF-8");        
         char[] buffer = new char[len];
         reader.read(buffer);
         return new String(buffer);
     }
    }

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment
	{

		public PlaceholderFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
