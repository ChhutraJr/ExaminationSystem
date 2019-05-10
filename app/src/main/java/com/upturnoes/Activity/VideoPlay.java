package com.upturnoes.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.upturnoes.Class.MyConfig;
import com.upturnoes.R;
import com.upturnoes.utils.MusicUtils;
import com.upturnoes.utils.Tools;

public class VideoPlay extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    VideoView video;
    private MediaController mediaController;
    String video_file;
  //  String video_url = MyConfig.Parent_Url+"assets/video/videoplayback.mp4";
    ProgressDialog pd;
    FloatingActionButton bt_play;
    String TAG = "VideoPlayer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_video_play);

        initToolbar();

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        video_file= sharedPreferences.getString("video_file",null);

        String video_url = MyConfig.Parent_Url+"assets/video/"+video_file;
Log.e("video_url",video_url);
        video = (VideoView)findViewById(R.id.video);
        bt_play = (FloatingActionButton) findViewById(R.id.bt_play);


        pd = new ProgressDialog(VideoPlay.this);
        pd.setMessage("Buffering video please wait...");
        pd.show();

        Uri uri = Uri.parse(video_url);
        video.setVideoURI(uri);

        mediaController = new MediaController(VideoPlay.this);
        mediaController.setAnchorView(video);
        video.setMediaController(mediaController);

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //close the progress dialog when buffering is done
                pd.dismiss();
                mp.setLooping(true);
                Log.e(TAG, "Duration = " + video.getDuration());


            }
        });
        video.start();


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Video");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }



}
