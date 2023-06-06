package com.example.musicplayernotification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class CreateNotification {


    public static final String CHANNEL_ID = "channel_1";
    public static final int notification_ID = 1;


    public static final String keyActionClose = "com.example.musicplayernotification.TERMINATE_APP";
    public static final String keyActionPrevious = "actionprevious";
    public static final String keyActionPlay = "actionplay";
    public static final String keyActionNext = "actionnext";
    public static final String keyChannelName = "KOD Dev";
    public static final int REQCODE_ActionPrevious = 1;
    public static final int REQCODE_ActionPlay = 2;
    public static final int REQCODE_ActionNext = 3;

    public static Notification notification;



    public static void createNotification(Context context, Track track
            , int playButton, int position, int size, boolean isCancel) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManagerCompat notificationManagerCompat
                    = NotificationManagerCompat.from(context);

            if(isCancel == true) {
                // Cancel the existing notification
                notificationManagerCompat.cancel(notification_ID);
                return;
            }

            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");

            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), track.getImage());




            // >> skip previous

            PendingIntent pendingIntentPrevious;
            int drw_previous;
            if (position == 0) {
                pendingIntentPrevious = null;
                drw_previous = 0;
            } else {
                Intent intentPrevious = new Intent(context, NotificationActionService.class)
                        .setAction(keyActionPrevious);
                pendingIntentPrevious = PendingIntent.getBroadcast(context, REQCODE_ActionPrevious
                        , intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);
                drw_previous = R.drawable.skip_previous;
            }

            // >> Play

            Intent intentPlay = new Intent(context, NotificationActionService.class)
                    .setAction(keyActionPlay);
            PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context, REQCODE_ActionPlay
                    , intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);

            // >> skip next

            PendingIntent pendingIntentNext;
            int drw_next;
            if (position == size) {
                pendingIntentNext = null;
                drw_next = 0;
            } else {
                Intent intentPrevious = new Intent(context, NotificationActionService.class)
                        .setAction(keyActionNext);
                pendingIntentNext = PendingIntent.getBroadcast(context, REQCODE_ActionNext
                        , intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);
                drw_next = R.drawable.skip_next;
            }

            // >> close

            Intent intentClose = new Intent(context, NotificationActionService.class)
                    .setAction(keyActionClose);
            PendingIntent pendingIntentClose = PendingIntent.getBroadcast(context, REQCODE_ActionPlay
                    , intentClose, PendingIntent.FLAG_UPDATE_CURRENT);
            int drw_close = R.drawable.close;

            // >> Create Notification
            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.music_note)
                    .setContentTitle(track.getTitle())
                    .setContentText(track.getArtist())
                    .setLargeIcon(icon)
                    .setOnlyAlertOnce(true) // showing notification for only first time.
                    .setShowWhen(false)
                    .addAction(drw_previous, "Previous", pendingIntentPrevious)
                    .addAction(playButton, "Play", pendingIntentPlay)
                    .addAction(drw_next, "Next", pendingIntentNext)
                    .addAction(drw_close, "Close", pendingIntentClose)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManagerCompat.notify(notification_ID, notification);

        }


    }
}
