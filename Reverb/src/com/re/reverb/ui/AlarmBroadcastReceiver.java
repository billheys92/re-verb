package com.re.reverb.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message
//        Toast.makeText(context, "Alarm Broadcast Receiver received message", Toast.LENGTH_SHORT).show();

//        if (intent.getAction().equals("com.re.reverb.intent.alarm.broadcast")) {

            Log.d("Re:verb Alarm Broadcast Receiver", "onReceive - "+intent.getAction());
//        }
    }
}