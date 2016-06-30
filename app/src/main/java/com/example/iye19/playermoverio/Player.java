package com.example.iye19.playermoverio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import jp.epson.moverio.bt200.DisplayControl;

public class Player extends AppCompatActivity {
    DisplayControl disp;
    int mode = DisplayControl.DISPLAY_MODE_2D;
    VideoView video;
    Handler time;

    SeekBar.OnSeekBarChangeListener sbcl = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser) {
                video.seekTo(progress);
                SeekBar bar2 = (SeekBar) findViewById(R.id.media3d).findViewById(R.id.media_izq).findViewById(R.id.seekBar);
                SeekBar bar3 = (SeekBar) findViewById(R.id.media3d).findViewById(R.id.media_der).findViewById(R.id.seekBar);
                bar2.setProgress(progress);
                bar3.setProgress(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    Runnable readTime = new Runnable() {
        @Override
        public void run() {
            SeekBar bar1 = (SeekBar)findViewById(R.id.media2d).findViewById(R.id.seekBar);
            SeekBar bar2 = (SeekBar)findViewById(R.id.media3d).findViewById(R.id.media_izq).findViewById(R.id.seekBar);
            SeekBar bar3 = (SeekBar)findViewById(R.id.media3d).findViewById(R.id.media_der).findViewById(R.id.seekBar);
            int millis = video.getCurrentPosition();
            bar1.setProgress(millis);
            bar2.setProgress(millis);
            bar3.setProgress(millis);
            time.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disp = new DisplayControl(this);
        setContentView(R.layout.activity_player);
        String uri = getIntent().getStringExtra("URI");
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.flags |= 0x80000000;
        window.setAttributes(params);
        video = (VideoView)findViewById(R.id.videoView);
        video.setVideoURI(Uri.parse(uri));
        video.requestFocus();
        video.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View media;
                if(mode == DisplayControl.DISPLAY_MODE_2D)
                    media = findViewById(R.id.media2d);
                else
                    media = findViewById(R.id.media3d);

                if(media.getVisibility() == View.VISIBLE)
                    media.setVisibility(View.INVISIBLE);
                else
                    media.setVisibility(View.VISIBLE);
                return false;
            }
        });
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                SeekBar bar1 = (SeekBar)findViewById(R.id.media2d).findViewById(R.id.seekBar);
                SeekBar bar2 = (SeekBar)findViewById(R.id.media3d).findViewById(R.id.media_izq).findViewById(R.id.seekBar);
                SeekBar bar3 = (SeekBar)findViewById(R.id.media3d).findViewById(R.id.media_der).findViewById(R.id.seekBar);
                int max = video.getDuration();
                bar1.setMax(max);
                bar1.setOnSeekBarChangeListener(sbcl);
                bar2.setMax(max);
                bar2.setOnSeekBarChangeListener(sbcl);
                bar3.setMax(max);
                bar3.setOnSeekBarChangeListener(sbcl);
                time = new Handler();
                time.post(readTime);
            }
        });
        video.start();
    }

    public void switchScreenMode(View v){
        View media2D = findViewById(R.id.media2d);
        View media3D = findViewById(R.id.media3d);

        if(mode == DisplayControl.DISPLAY_MODE_2D) {
            mode = DisplayControl.DISPLAY_MODE_3D;
            media2D.setVisibility(View.INVISIBLE);
            media3D.setVisibility(View.VISIBLE);
        }

        else {
            mode = DisplayControl.DISPLAY_MODE_2D;
            media3D.setVisibility(View.INVISIBLE);
            media2D.setVisibility(View.VISIBLE);
        }

        //disp.setMode(mode, false);
    }

    public void play(View v){
        ImageButton b1, b2, b3;

        b1 = (ImageButton)findViewById(R.id.media3d).findViewById(R.id.media_izq).findViewById(R.id.button_play);
        b2 = (ImageButton)findViewById(R.id.media3d).findViewById(R.id.media_der).findViewById(R.id.button_play);
        b3 = (ImageButton)findViewById(R.id.media2d).findViewById(R.id.button_play);

        int image;

        if(video.isPlaying()){
            video.pause();
            image = android.R.drawable.ic_media_play;
        }
        else{
            video.start();
            image = android.R.drawable.ic_media_pause;
        }

        b1.setImageResource(image);
        b2.setImageResource(image);
        b3.setImageResource(image);
    }
}
