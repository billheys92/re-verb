package com.re.reverb.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.re.reverb.R;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.network.UpdateManagerImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bill on 2015-03-13.
 */
public class BackgroundUpdateService extends Service
{
    private static String lastUpdateTimestamp;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mPrefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
        lastUpdateTimestamp = mPrefs.getString("LAST_BACKGROUND_UPDATE_TIME", null);

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
        String nowString = sdf.format(now);
        mEditor.putString("LAST_BACKGROUND_UPDATE_TIME", nowString);
        mEditor.commit();

        Log.d("Testing", "Service got created");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
//        Log.d("Testing", "Service got started");
//        Toast.makeText(this, "re:verb background update", Toast.LENGTH_SHORT).show();

        try
        {
            UpdateManagerImpl.getNewUpdates(lastUpdateTimestamp);
        } catch (NotSignedInException e)
        {
            e.printStackTrace();
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("re:verb notification")
                        .setContentText("Updates since "+lastUpdateTimestamp);

        Intent resultIntent = new Intent(this, MainViewPagerActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainViewPagerActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        stopSelf();
    }
}
