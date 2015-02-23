package com.re.reverb.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.re.reverb.R;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.account.UserProfile;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;

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
    private static View view;

    UserProfile profile = new UserProfile("test@test.com","bheys","Bill Heys","description", "token", 1);
    private ImageView backgroundMapImageView;
    NetworkImageView profilePic;

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

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        } catch (InflateException e) {
            /* fragment is already there, just return view as it is */
        }
        /*Fragment postListFragment = new PostListFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.userProfilePostListFragment, postListFragment).commit();
*/
		TextView nameText = (TextView) view.findViewById(R.id.nameTextView);
        nameText.setText(profile.getUsername());
		TextView handleText = (TextView) view.findViewById(R.id.handleTextView);
        handleText.setText(profile.getHandle());
        TextView descriptionText = (TextView) view.findViewById(R.id.userDescription);
        descriptionText.setText(profile.getDescription());
        TextView emailText = (TextView) view.findViewById(R.id.emailTextView);
        emailText.setText(profile.getEmail());
        profilePic = (NetworkImageView) view.findViewById(R.id.profilePicture);
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
            Bitmap bm = getGoogleMapThumbnail(Reverb.getInstance().locationManager.getCurrentLatitude(),Reverb.getInstance().locationManager.getCurrentLongitude());
            return bm;

        }

    };

    public Bitmap getGoogleMapThumbnail(double latitude, double longitude){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String mapType = sharedPrefs.getString("pref_map_type","roadmap");
        String URL = "http://maps.google.com/maps/api/staticmap?center=" +latitude + "," + longitude + "&zoom=13&size=600x600&sensor=false&maptype="+mapType;

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
