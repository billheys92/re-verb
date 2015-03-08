package com.re.reverb.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.re.reverb.R;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.account.UpdateAboutMeDto;
import com.re.reverb.androidBackend.account.UpdateHandleDto;
import com.re.reverb.androidBackend.account.UpdateUsernameDto;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.utils.GenericOverLay;
import com.re.reverb.network.AccountManagerImpl;

public abstract class OverlayFragment extends ListFragment
{
    GenericOverLay logoutEditOverlay;
    GenericOverLay editUserInfoOverlay;

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
                        UpdateUsernameDto updateUsernameDto = new UpdateUsernameDto(userId, ((EditText)activity.findViewById(R.id.edit_username)).getText().toString());
                        AccountManagerImpl.updateUsername(updateUsernameDto);
                    }
                    if(!(((EditText)activity.findViewById(R.id.edit_handle)).getText().toString() == null || ((EditText)activity.findViewById(R.id.edit_handle)).getText().toString().isEmpty()))
                    {
                        UpdateHandleDto updateHandleDto = new UpdateHandleDto(userId, ((EditText)activity.findViewById(R.id.edit_handle)).getText().toString());
                        AccountManagerImpl.updateHandle(activity, updateHandleDto);
                    }
                    if(!(((EditText)activity.findViewById(R.id.edit_aboutMe)).getText().toString() == null || ((EditText)activity.findViewById(R.id.edit_aboutMe)).getText().toString().isEmpty()))
                    {
                        UpdateAboutMeDto updateAboutMeDto = new UpdateAboutMeDto(userId, ((EditText)activity.findViewById(R.id.edit_aboutMe)).getText().toString());
                        AccountManagerImpl.updateAboutMe(updateAboutMeDto);
                    }
                    editUserInfoOverlay.removeOverlays();
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
}
