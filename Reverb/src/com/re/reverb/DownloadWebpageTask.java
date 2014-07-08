package com.re.reverb;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class DownloadWebpageTask extends AsyncTask<String, Void, String> {
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
    	//EditText ed = (EditText)findViewById(R.id.edit_message);
    	//ed.setHint(result);
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


