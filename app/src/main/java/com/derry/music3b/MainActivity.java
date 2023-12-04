package com.derry.music3b;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button playButton;
    SeekBar start,end;
    TextView startText,endText;
    MediaPlayer song;
    ImageView imageView;
    Animation animation;
    int SongTotalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //To hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Id implementation
        playButton=findViewById(R.id.play);
        startText = findViewById(R.id.TextStart);
        endText = findViewById(R.id.TextEnd);
        imageView = findViewById(R.id.img);
        animation = AnimationUtils.loadAnimation(this,R.anim.rotation);

        //Song Added
        song = MediaPlayer.create(this,R.raw.song);
        song.setLooping(true);
        song.seekTo(0);
        song.setVolume(0.5f,0.5f);
        SongTotalTime = song.getDuration();

        //Control Seek bar track line / play line
        start = findViewById(R.id.PlayLine);
        start.setMax(SongTotalTime);
        start.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    song.seekTo(i);
                    start.setProgress(i);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Volume control
        end = findViewById(R.id.volume);
        end.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float volume = i /100f;
                song.setVolume(volume,volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Up date song time line
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (song != null){
                    try {
                        Message message = new Message();
                        message.what = song.getCurrentPosition();
                        handler.sendMessage(message);
                        Thread.sleep(1000);
                    }catch (InterruptedException ignored){

                    }
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @SuppressLint("SetText18n")
        @Override
        public void handleMessage(Message message){
            int SeekBarPosition=message.what;

            //Update song seek bar
            start.setProgress(SeekBarPosition);

            //update Labels
            String Time=createTimeText(SeekBarPosition);
            startText.setText(Time);

            //Time calculation
            String remainingTime =createTimeText(SongTotalTime - SeekBarPosition);
            endText.setText("-"+remainingTime);
        }
    };

    //Time Shows
    public String createTimeText(int time){
        String timeText;
        int min=time / 1000 / 60;
        int sec = time / 1000 % 60;
        timeText = min + ":";
        if(sec < 10) timeText+="0";
        timeText += sec;
        return timeText;
    }

    public void PlayButton(View view){
        if(!song.isPlaying()){
            //Stopped
            song.start();
            //Rotation start
            imageView.startAnimation(animation);
            playButton.setBackgroundResource(R.drawable.baseline_pause_24);
        }else {
            //Played
            song.pause();
            imageView.clearAnimation();
            playButton.setBackgroundResource(R.drawable.baseline_play_arrow_24);
        }
    }

}