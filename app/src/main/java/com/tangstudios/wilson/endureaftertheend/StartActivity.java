package com.tangstudios.wilson.endureaftertheend;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;


public class StartActivity extends Activity {

    MediaPlayer backgroundMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        TextView title = (TextView) findViewById(R.id.textView);
        TextView subtitle = (TextView) findViewById(R.id.textView2);

        Typeface monospace = Typeface.createFromAsset(getAssets(), "fonts/712_serif.ttf");

        title.setTypeface(monospace);
        subtitle.setTypeface(monospace);

        backgroundMusic = MediaPlayer.create(this, R.raw.start_screen_music);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
            float vol = 0.3f; //This will be half of the default system sound
            am.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);

            SharedPreferences settings = getSharedPreferences("User Settings", Context.MODE_PRIVATE);
            int path = settings.getInt("PATH", 0);

            if (path == 0) {
                Intent play = new Intent(this, EnterNameActivity.class);
                startActivity(play);
                finish();
            } else {
                Intent play = new Intent(this, ContinueActivity.class);
                startActivity(play);
                finish();
            }

            return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onStop() {
        backgroundMusic.stop();
        super.onStop();
    }

    @Override
    public void onRestart() {
        backgroundMusic.start();
        super.onRestart();
    }
}