package com.re.reverb.ui;

import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.re.reverb.network.AccountManagerImpl;

import java.io.IOException;

public class GetUsernameTask extends AsyncTask<Object, Void, Void>
{
    SplashScreenActivity mActivity;
    String mScope;
    String mEmail;
    String token;

    GetUsernameTask(SplashScreenActivity activity, String name, String scope) {
        this.mActivity = activity;
        this.mScope = scope;
        this.mEmail = name;
    }

    /**
     * Executes the asynchronous job. This runs when you call execute()
     * on the AsyncTask instance.
     */
    @Override
    protected Void doInBackground(Object[] params)
    {
        try
        {
            token = fetchToken();
            if (token != null)
            {
                // Insert the good stuff here.
                // Use the token to access the user's Google data.
            }
        } catch (IOException e)
        {
            // The fetchToken() method handles Google-specific exceptions,
            // so this indicates something went wrong at a higher level.
            // TIP: Check for network connectivity before starting the AsyncTask.

        }
        return null;
    }

    /**
     * Gets an authentication token from Google and handles any
     * GoogleAuthException that may occur.
     */
    protected String fetchToken() throws IOException {
        try {
            return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
        } catch (UserRecoverableAuthException userRecoverableException) {
            // GooglePlayServices.apk is either old, disabled, or not present
            // so we need to show the user some UI in the activity to recover.
            mActivity.handleException(userRecoverableException);
        } catch (GoogleAuthException fatalException) {
            // Some other type of unrecoverable exception has occurred.
            // Report and log the error as appropriate for your app.

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v)
    {
        if(mEmail != null && token != null)
        {
            AccountManagerImpl.getUserExists(mEmail, token, mActivity);
        }
    }
}