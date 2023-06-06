package com.example.musicplayernotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationActionService extends BroadcastReceiver {

    private static final String ACTION_TERMINATE_APP = "com.example.musicplayernotification.TERMINATE_APP";

    @Override
    public void onReceive(Context context, Intent intent) {
//        context.sendBroadcast(new Intent("TRACKS_TRACKS")
//                .putExtra("actionname", intent.getAction()));

        String action = intent.getAction();

        if (ACTION_TERMINATE_APP.equals(action)) {
            // Perform any necessary cleanup or save data before termination
            // ...

            CreateNotification.createNotification(context, null
                    , 0, 0, 0, true);
            // Terminate the application
            System.exit(0);
        } else {
            // Handle other actions
            context.sendBroadcast(new Intent("TRACKS_TRACKS")
                    .putExtra("actionname", action));
        }
    }
}
