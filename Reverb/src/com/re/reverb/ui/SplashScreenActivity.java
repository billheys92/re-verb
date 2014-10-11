package com.re.reverb.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.re.reverb.R;

/**
 * Created by Bill on 2014-10-10.
 */
public class SplashScreenActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        testInternetConnection();
        if(!testInternetConnection()) {
            makeNoConnectionToast();
        }
    }

    public void onScreenClick(View view){
        if(testInternetConnection()) {
            Intent intent = new Intent(this, MainViewPagerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
        }
        else {
            makeNoConnectionToast();
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
}
