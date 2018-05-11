package com.example.michal.mediaplayer;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private int chosen_track = -1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = new MediaPlayer();

        checkForPermissions();

        ListView musicMediaListView = (ListView)findViewById(R.id.music_media_listview);
        final Cursor musicMediaCursor = getMusicMediaCursor();
        musicMediaListView.setAdapter(new MusicMediaCursorAdapter(this, musicMediaCursor));


        musicMediaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                musicMediaCursor.moveToPosition(position);
                String path = musicMediaCursor.getString(musicMediaCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                Log.i("my_app", path);

                if (position != chosen_track) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(path);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                }
                else {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    else {
                        mediaPlayer.start();
                    }
                }
                chosen_track = position;
            }
        });
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }

    private Cursor getMusicMediaCursor() {
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.IS_MUSIC + " = 1",
                null,
                MediaStore.Audio.Media.TITLE + " ASC");
        cursor.moveToFirst();
        return cursor;
    }

    private void checkForPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }
}
