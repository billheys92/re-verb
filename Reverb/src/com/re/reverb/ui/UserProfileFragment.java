package com.re.reverb.ui;

import com.re.reverb.R;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.UserProfile;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class UserProfileFragment extends Fragment
{

    private static final int SELECT_PHOTO = 100;

    UserProfile profile;
    private ImageView backgroundMapImageView;
    ImageButton profilePic;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
        try {
            profile = Reverb.getInstance().getCurrentUser();
        } catch (NotSignedInException e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        /*Fragment postListFragment = new PostListFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.userProfilePostListFragment, postListFragment).commit();
*/
		TextView nameText = (TextView) view.findViewById(R.id.nameTextView);
        nameText.setText(profile.getName());
		TextView handleText = (TextView) view.findViewById(R.id.handleTextView);
        handleText.setText(profile.getNickname());
        TextView descriptionText = (TextView) view.findViewById(R.id.userDescription);
        descriptionText.setText(profile.getDescription());
        TextView emailText = (TextView) view.findViewById(R.id.emailTextView);
        emailText.setText(profile.getEmail());
        profilePic = (ImageButton) view.findViewById(R.id.profilePicture);
        profilePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("Reverb","profile picture clicked");
                choosePictureFromGallery();
            }

        });
        backgroundMapImageView = (ImageView) view.findViewById(R.id.userinfo);
        new SendTask().execute();

        return view;
	}

    private void choosePictureFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == Activity.RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    profilePic.setImageBitmap(BitmapFactory.decodeStream(imageStream));
                }
        }
    }

    private class SendTask extends AsyncTask<Bitmap, String, Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bmp){
            backgroundMapImageView.setImageBitmap(bmp);
        }

        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            Bitmap bm = getGoogleMapThumbnail(47.5641001,-52.7015303);
            return bm;

        }

    };

    public static Bitmap getGoogleMapThumbnail(double latitude, double longitude){

        String mapType = Reverb.getInstance().getSettings().getMapType();
        String URL = "http://maps.google.com/maps/api/staticmap?center=" +latitude + "," + longitude + "&zoom=12&size=600x600&sensor=false&maptype="+mapType;

        Bitmap bmp = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);

        InputStream in;
        try {
            in = httpclient.execute(request).getEntity().getContent();
            bmp = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp;
    }

}
