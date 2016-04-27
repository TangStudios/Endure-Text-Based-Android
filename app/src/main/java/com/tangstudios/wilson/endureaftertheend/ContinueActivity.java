package com.tangstudios.wilson.endureaftertheend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Wilson on 6/30/2015.
 */
public class ContinueActivity extends Activity {

    TextView asdf;
    TextView welcomeBack;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.continue_layout);
        
        welcomeBack = (TextView) findViewById(R.id.welcomeBack);
        asdf = (TextView) findViewById(R.id.asdf);

        Typeface monospace = Typeface.createFromAsset(getAssets(), "fonts/712_serif.ttf");
        
        welcomeBack.setTypeface(monospace);
        asdf.setTypeface(monospace);

        SharedPreferences settings = getSharedPreferences("User Settings", Context.MODE_PRIVATE);

        String usersName = settings.getString("USERS NAME", "EMPTY");

        welcomeBack.setText(usersName + "!");
    }
    
    public void onContinueStory(View view) {
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        float vol = 0.3f; //This will be half of the default system sound
        am.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);

        Intent toConsole = new Intent(this, Console.class);
        startActivity(toConsole);
        finish();
    }
    
    public void onRestartStory(View view) {
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        float vol = 0.3f; //This will be half of the default system sound
        am.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);

        SharedPreferences settings = getSharedPreferences("User Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("USERS NAME", "");
        editor.putInt("PATH", 0);
        editor.putInt("HEALTH", 100);
        editor.putInt("HUNGER", 10);
        editor.putInt("THIRST", 10);
        editor.putInt("FOOD", 3);
        editor.putInt("WATER", 3);
        editor.putInt("SWORD DAMAGE", 10);
        editor.putInt("BULLETS", 3);
        editor.putInt("EVENT 1 ENEMY HEALTH", 100);
        editor.putInt("EVENT 2 ENEMY HEALTH", 100);
        editor.putInt("EVENT 3 ENEMY HEALTH", 100);
        editor.putInt("EVENT 4 ENEMY HEALTH", 200);

        editor.commit();
        
        Intent play = new Intent(this, EnterNameActivity.class);
        startActivity(play);
        finish();
    }
}
