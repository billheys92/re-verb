package com.re.reverb.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.TextView;

import com.re.reverb.R;
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


        //Onclick listener for save user info button here

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
