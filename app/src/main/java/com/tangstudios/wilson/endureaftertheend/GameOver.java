package com.tangstudios.wilson.endureaftertheend;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Wilson on 6/27/2015.
 */
public class GameOver extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_over_layout);

        Typeface monospace = Typeface.createFromAsset(getAssets(), "fonts/712_serif.ttf");

        TextView title = (TextView) findViewById(R.id.game_over);
        TextView subtitle = (TextView) findViewById(R.id.created);

        title.setTypeface(monospace);
        subtitle.setTypeface(monospace);
    }

    public void onRestart(View view) {
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        float vol = 0.3f; //This will be half of the default system sound
        am.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);

        Intent toHome = new Intent(this, StartActivity.class);
        startActivity(toHome);
        finish();
    }
}
