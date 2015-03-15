package com.re.reverb.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.LocationSource;
import com.re.reverb.R;
import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.LocationUpdateListener;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.account.UserProfile;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.androidBackend.feed.UserPostFeed;
import com.re.reverb.androidBackend.post.dto.PostActionDto;
import com.re.reverb.network.PostManagerImpl;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class UserProfileFragment extends FeedFragment implements LocationUpdateListener
{

    private static final int SELECT_PHOTO = 100;
    private static View view;

    UserProfile profile = new UserProfile("test@test.com","bheys","Bill Heys","description", "token", 1);
    private ImageView backgroundMapImageView;
    NetworkImageView profilePic;

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

		TextView nameText = (TextView) view.findViewById(R.id.nameTextView);
        nameText.setText(profile.Name);
		TextView handleText = (TextView) view.findViewById(R.id.handleTextView);
        handleText.setText(profile.Handle);
        TextView descriptionText = (TextView) view.findViewById(R.id.userDescription);
        descriptionText.setText(profile.About_me);
        TextView emailText = (TextView) view.findViewById(R.id.emailTextView);
        emailText.setText(profile.Email_address);
        profilePic = (NetworkImageView) view.findViewById(R.id.profilePicture);
        profilePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("Reverb","profile picture clicked");
                choosePictureFromGallery();
            }

        });
        backgroundMapImageView = (ImageView) view.findViewById(R.id.userinfo);
//        new SendTask().execute();
        Reverb.getInstance().attachLocationListener(this);

        view = setupDataFeed(view, new UserPostFeed());
        ((ReverbActivity) getActivity()).setupUIBasedOnAnonymity(Reverb.getInstance().isAnonymous());

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

    @Override
    public void onRefresh()
    {
        try
        {
            dataFeed.refreshPosts();
            adapter.notifyDataSetChanged();
        } catch (UnsuccessfulRefreshException e)
        {
            Toast.makeText(getActivity(), R.string.refresh_error_toast_message, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (NotSignedInException e)
        {
            Toast.makeText(getActivity(), R.string.not_signed_in_message, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    public void updateUserInfo()
    {
        TextView nameText = (TextView) view.findViewById(R.id.nameTextView);
        nameText.setText(profile.Name);
        TextView handleText = (TextView) view.findViewById(R.id.handleTextView);
        handleText.setText(profile.Handle);
        TextView descriptionText = (TextView) view.findViewById(R.id.userDescription);
        descriptionText.setText(profile.About_me);
        TextView emailText = (TextView) view.findViewById(R.id.emailTextView);
        emailText.setText(profile.Email_address);
    }

    @Override
    public void onLocationChanged(Location newLocation)
    {
        new SendTask().execute();
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

        String URL = "http://maps.google.com/maps/api/staticmap?center=" +latitude + "," + longitude + "&zoom=13&size=600x600&sensor=false&maptype=roadmap";

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

    @Override
    public void onOpenOverlayClick(final int messageId)
    {
        final Activity activity = this.getActivity();
        displayOverlay(R.layout.overlay_more_options_user, R.id.overlayUserFeedLayoutContainer);
        RelativeLayout deletePostRow = (RelativeLayout)activity.findViewById(R.id.deletePostRow);
        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    PostActionDto postActionDto = new PostActionDto(messageId, Reverb.getInstance().getCurrentUserId());
                    PostManagerImpl.deletePost(postActionDto, activity);
                } catch (NotSignedInException e)
                {
                    e.printStackTrace();
                }
                removeOverlays();
            }
        };
        deletePostRow.setOnClickListener(listener);

        View.OnClickListener exitListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                removeOverlays();
            }
        };
        (activity.findViewById(R.id.userFeedOverlayLayout)).setOnClickListener(exitListener);
    }

    @Override
    protected void extraAnonymousUISetup()
    {

    }

    @Override
    protected void extraPublicUISetup()
    {

    }

    @Override
    public void onOpenLogoutEditOverlayClick()
    {
        standardOnOpenLogoutEditOverlayClick(R.id.overlayUserFeedLayoutContainer);
    }

    @Override
    public void onEditUserInfoOverlayClick()
    {
        standardOnEditUserInfoOverlayClick(R.id.overlayUserFeedLayoutContainer);
    }

}
