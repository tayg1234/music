package com.cookandroid.music;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        TextView songTitle = findViewById(R.id.songTitle);
        seekBar = findViewById(R.id.seekBar);

        String title = getIntent().getStringExtra("songTitle");
        songTitle.setText(title);

        // 변경된 파일 이름 사용
        mediaPlayer = MediaPlayer.create(this, R.raw.unavailable);  // 파일 이름 변경

        mediaPlayer.start();

        seekBar.setMax(mediaPlayer.getDuration());

        handler = new Handler(Looper.getMainLooper());

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(updateSeekBar, 0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        handler.removeCallbacks(updateSeekBar);
        super.onBackPressed();
    }
}