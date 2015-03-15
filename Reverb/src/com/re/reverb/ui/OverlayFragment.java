package com.re.reverb.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.re.reverb.R;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.account.UpdateAboutMeDto;
import com.re.reverb.androidBackend.account.UpdateHandleDto;
import com.re.reverb.androidBackend.account.UpdateProfilePictureDto;
import com.re.reverb.androidBackend.account.UpdateUsernameDto;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.utils.GenericOverLay;
import com.re.reverb.network.AccountManagerImpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class OverlayFragment extends ListFragment
{
    GenericOverLay logoutEditOverlay;
    GenericOverLay editUserInfoOverlay;
    private static final int SELECT_PHOTO = 100;

    ImageView profilePic;

    public abstract void onOpenLogoutEditOverlayClick();

    public abstract void onEditUserInfoOverlayClick();

    protected void standardOnOpenLogoutEditOverlayClick(int containerId)
    {
        final Activity activity = this.getActivity();
        logoutEditOverlay.displayOverlay(R.layout.overlay_logout_edit_info, containerId);
        TextView logoutUserRow = (TextView)activity.findViewById(R.id.logoutUser);
        View.OnClickListener logoutUserRowListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                logoutEditOverlay.removeOverlays();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SplashScreenActivity.USER_EMAIL, SplashScreenActivity.NO_SAVED_EMAIL);
                editor.commit();
                System.exit(2);
                Intent i = activity.getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( activity.getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                //AccountManagerImpl.logoutUser(activity);
            }
        };
        logoutUserRow.setOnClickListener(logoutUserRowListener);

        TextView editUserInfoRow = (TextView)activity.findViewById(R.id.editUserInfo);
        View.OnClickListener editUserInfoRowListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                logoutEditOverlay.removeOverlays();
                onEditUserInfoOverlayClick();
            }
        };
        editUserInfoRow.setOnClickListener(editUserInfoRowListener);

        View.OnClickListener exitListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                logoutEditOverlay.removeOverlays();
            }
        };
        (activity.findViewById(R.id.logoutEditOverlay)).setOnClickListener(exitListener);
    }

    protected void standardOnEditUserInfoOverlayClick(int containerId)
    {
        final Activity activity = this.getActivity();
        editUserInfoOverlay.displayOverlay(R.layout.overlay_edit_user_info, containerId);
        profilePic = (ImageView)activity.findViewById(R.id.edit_profilePicture);
        profilePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                choosePictureFromGallery();
            }

        });

        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    int userId = Reverb.getInstance().getCurrentUserId();

                    if(!(((EditText)activity.findViewById(R.id.edit_username)).getText().toString() == null || ((EditText)activity.findViewById(R.id.edit_username)).getText().toString().isEmpty()))
                    {
                        String text = ((EditText)activity.findViewById(R.id.edit_username)).getText().toString();
                        UpdateUsernameDto updateUsernameDto = new UpdateUsernameDto(userId, text);
                        AccountManagerImpl.updateUsername(updateUsernameDto);
                        Reverb.getInstance().getCurrentUser().Name = text;
                    }
                    if(!(((EditText)activity.findViewById(R.id.edit_handle)).getText().toString() == null || ((EditText)activity.findViewById(R.id.edit_handle)).getText().toString().isEmpty()))
                    {
                        String text = ((EditText)activity.findViewById(R.id.edit_handle)).getText().toString();
                        UpdateHandleDto updateHandleDto = new UpdateHandleDto(userId, text);
                        AccountManagerImpl.updateHandle(activity, updateHandleDto);
                        Reverb.getInstance().getCurrentUser().Handle = text;
                    }
                    if(!(((EditText)activity.findViewById(R.id.edit_aboutMe)).getText().toString() == null || ((EditText)activity.findViewById(R.id.edit_aboutMe)).getText().toString().isEmpty()))
                    {
                        String text = ((EditText)activity.findViewById(R.id.edit_aboutMe)).getText().toString();
                        UpdateAboutMeDto updateAboutMeDto = new UpdateAboutMeDto(userId, text);
                        AccountManagerImpl.updateAboutMe(updateAboutMeDto);
                        Reverb.getInstance().getCurrentUser().About_me = text;
                    }
                    if(((ImageView)activity.findViewById(R.id.edit_profilePicture)).getDrawable() != null)
                    {
                        UpdateProfilePictureDto updateProfilePictureDto = new UpdateProfilePictureDto(userId, null);
                        AccountManagerImpl.updateProfilePicture(updateProfilePictureDto, attachPhoto());
                    }
                    editUserInfoOverlay.removeOverlays();
                    if(activity instanceof MainViewPagerActivity)
                    {
                        ((MainViewPagerActivity)activity).updateUserInfo();
                    }
                } catch (NotSignedInException e)
                {
                    Toast.makeText(activity, R.string.not_signed_in_message, Toast.LENGTH_SHORT).show();
                }
            }
        };
        (activity.findViewById(R.id.saveUserInfoButton)).setOnClickListener(listener);

        View.OnClickListener exitListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                editUserInfoOverlay.removeOverlays();
            }
        };
        (activity.findViewById(R.id.editUserInfoOverlay)).setOnClickListener(exitListener);
    }

    protected File attachPhoto()
    {
        File f = new File(this.getActivity().getCacheDir(), "profilePicture");
        //Convert bitmap to byte array
        Bitmap bitmap = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();

        bitmap = ThumbnailUtils.extractThumbnail(bitmap, profilePic.getWidth(), profilePic.getWidth());

        //Bitmap bitmap = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(f);
            try
            {
                fos.write(bitmapdata);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return f;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == SELECT_PHOTO)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try
                {
                    imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    profilePic.setImageBitmap(BitmapFactory.decodeStream(imageStream));
                    TextView selectingText = (TextView) getActivity().findViewById(R.id.edit_selectingPictureText);
                    selectingText.setVisibility(View.INVISIBLE);
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void choosePictureFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        getActivity().startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }
}
