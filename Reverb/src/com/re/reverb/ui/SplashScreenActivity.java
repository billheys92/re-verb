package com.re.reverb.ui;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.re.reverb.R;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.account.CreateUserDto;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.network.AccountManagerImpl;
import com.re.reverb.network.RequestQueueSingleton;

/**
 * Created by Bill on 2014-10-10.
 */
public class SplashScreenActivity extends Activity
{
    String mEmail; // Received from newChooseAccountIntent(); passed to getToken()
    private View currentOverlay = null;

    public static final String USER_EMAIL = "userEmail";
    public static final String NO_SAVED_EMAIL = "no saved email";
    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1002;


    private static final String EMAIL_KEY = "EMAIL_KEY";
    private static final String TOKEN_KEY = "TOKEN_KEY";

    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT)
        {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK)
            {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                // Save the shared preference
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USER_EMAIL, mEmail);
                editor.apply();

                // With the account name acquired, go get the auth token
                getUsername();
            } else if (resultCode == RESULT_CANCELED)
            {
                // The account picker dialog closed without selecting an account.
                // Notify users that they must pick an account to proceed.
                Toast.makeText(this, "Pick an Account", Toast.LENGTH_SHORT).show();
            }
        }
        else if ((requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR ||
                requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
                && resultCode == RESULT_OK)
        {
            // Receiving a result that follows a GoogleAuthException, try auth again
            getUsername();
        }
    }

    private static final String SCOPE =
            "oauth2:https://www.googleapis.com/auth/userinfo.profile";

    /**
     * Attempts to retrieve the username.
     * If the account is not yet known, invoke the picker. Once the account is known,
     * start an instance of the AsyncTask to get the auth token and do work with it.
     */
    private void getUsername() {
        if (mEmail == null) {
            pickUserAccount();
        } else {
            if (testInternetConnection()) {
                new GetUsernameTask(this, mEmail, SCOPE).execute();
            } else {
                Toast.makeText(this, "Not Online", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * This method is a hook for background threads and async tasks that need to
     * provide the user a response UI when an exception occurs.
     */
    public void handleException(final Exception e) {
        // Because this call comes from the AsyncTask, we must ensure that the following
        // code instead executes on the UI thread.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException)e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            SplashScreenActivity.this,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException)e).getIntent();
                    startActivityForResult(intent,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        testInternetConnection();
        if(!testInternetConnection()) {
            makeNoConnectionToast();
        }

        RequestQueueSingleton.getInstance(this.getApplicationContext());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String userEmail = sharedPreferences.getString(USER_EMAIL, NO_SAVED_EMAIL);
        if(userEmail.equals(NO_SAVED_EMAIL))
        {
            pickUserAccount();
        }
        else
        {
            mEmail = userEmail;
            getUsername();
        }

    }

    public void onUserDoesNotExist(final String email, final String token)
    {
        //CHANGE TO OVERLAY VIEW
        final SplashScreenActivity activity = this;
        displayOverlay(R.layout.overlay_create_user);
        Button b = (Button)findViewById(R.id.createUserButton);
        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(((EditText)findViewById(R.id.username)).getText().toString() == null || ((EditText)findViewById(R.id.username)).getText().toString().isEmpty())
                {
                    Toast.makeText(activity, "You must provide username.",Toast.LENGTH_SHORT).show();
                }
                else if(((EditText)findViewById(R.id.handle)).getText().toString() == null || ((EditText)findViewById(R.id.handle)).getText().toString().isEmpty())
                {
                    Toast.makeText(activity, "You must provide handle.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String aboutMe = ((EditText)findViewById(R.id.aboutMe)).getText().toString() == null ? "" : ((EditText)findViewById(R.id.aboutMe)).getText().toString();
                    CreateUserDto createUserDto = new CreateUserDto(
                            ((EditText)findViewById(R.id.username)).getText().toString(),
                            ((EditText)findViewById(R.id.handle)).getText().toString(),
                            email,
                            token,
                            aboutMe
                    );

                    AccountManagerImpl.createUser(createUserDto, activity);
                }
            }
        };
        b.setOnClickListener(listener);
    }

    private View displayOverlay(int resource)
    {
        removeOverlays();
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(resource, null);
        this.currentOverlay = v;

        //insert into slash screen
        FrameLayout myLayout = (FrameLayout) findViewById(R.id.overlaySplashScreenContainerLayout);
        myLayout.addView(v);
        return v;

    }

    public void removeOverlays()
    {
        if(this.currentOverlay != null)
        {
            ViewGroup parent = (ViewGroup) this.currentOverlay.getParent();
            parent.removeView(this.currentOverlay);
            this.currentOverlay = null;
        }
    }

    public void onScreenClick(View view)
    {
        try
        {
            if(Reverb.getInstance().getCurrentUser() != null)
            {
                if (testInternetConnection())
                {
                    Intent intent = new Intent(this, MainViewPagerActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                }
                else
                {
                    makeNoConnectionToast();
                }
            }
        } catch (NotSignedInException e)
        {
            Toast.makeText(this, R.string.not_signed_in_message, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean testInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void makeNoConnectionToast() {
        Toast.makeText(this, R.string.no_network_toast, Toast.LENGTH_SHORT).show();
    }

    public void onExit()
    {
        finish();
    }
}
