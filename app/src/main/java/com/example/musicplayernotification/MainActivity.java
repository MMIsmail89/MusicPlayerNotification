package com.example.musicplayernotification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.musicplayernotification.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Playable{

    private ActivityMainBinding binding;
    NotificationManager notificationManager;
    int position = 0;
    boolean isPlaying = false;

    List<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //
        populateTracks();
        //
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        }
        //
        binding.mainIbtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    onTrackPause();
                }
                else {
                    onTrackPlay();
                }
//                CreateNotification.createNotification(MainActivity.this,
//                        tracks.get(1), R.drawable.play, 1, tracks.size()-1);
            }
        });
        //


    }

    private void createChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    CreateNotification.keyChannelName, NotificationManager.IMPORTANCE_LOW);

            notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager !=null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void populateTracks() {
        tracks = new ArrayList<>();

        tracks.add(new Track("Ahmed Talk", "Artist Abo-Hemeed", R.drawable.ahmed));
        tracks.add(new Track("Basma Talk", "Artist Be-zoo", R.drawable.basma));
        tracks.add(new Track("Cari Talk", "Artist Cari of Deck-our", R.drawable.cari));
        tracks.add(new Track("Do Ri Me Talk", "Artist Do if you need", R.drawable.dorime));
    }
    //
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");

            switch (action){
                case CreateNotification.keyActionPrevious:
                {
                    onTrackPrevious();
                    break;
                }
                case CreateNotification.keyActionPlay: {
                    if (isPlaying) {
                        onTrackPause();
                    } else {
                        onTrackPlay();
                    }
                    break;
                }
                case CreateNotification.keyActionNext:
                {
                    onTrackNext();
                    break;
                }

            }
        }
    };


    @Override
    public void onTrackPrevious() {
        position--;
        CreateNotification.createNotification(MainActivity.this, tracks.get(position)
                , R.drawable.pause, position, tracks.size()-1, false);
        binding.mainTvTitle.setText(tracks.get(position).getTitle());
    }

    @Override
    public void onTrackPlay() {
        CreateNotification.createNotification(MainActivity.this, tracks.get(position)
                , R.drawable.pause, position, tracks.size()-1, false);

        binding.mainIbtnPlay.setImageResource(R.drawable.pause);

        binding.mainTvTitle.setText(tracks.get(position).getTitle());
        isPlaying = true;
    }

    @Override
    public void onTrackPause() {
        CreateNotification.createNotification(MainActivity.this, tracks.get(position)
                , R.drawable.play, position, tracks.size()-1,false);

        binding.mainIbtnPlay.setImageResource(R.drawable.play);

        binding.mainTvTitle.setText(tracks.get(position).getTitle());
        isPlaying = false;
    }

    @Override
    public void onTrackNext() {
        position++;
        CreateNotification.createNotification(MainActivity.this, tracks.get(position)
                , R.drawable.pause, position, tracks.size()-1, false);
        binding.mainTvTitle.setText(tracks.get(position).getTitle());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            notificationManager.cancelAll();
        }
        //
        unregisterReceiver(broadcastReceiver);
    }
}