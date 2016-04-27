package com.tangstudios.wilson.endureaftertheend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Wilson on 6/29/2015.
 */
public class GameEnd extends Activity {

    int score;
    int health;
    int hunger;
    int thirst;
    int swordDamage;
    int food;
    int water;
    int bullets;
    int path;

    String usersName;

    TextView nameText;
    TextView scoreText;

    MediaPlayer backgroundMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_end_layout);

        Typeface monospace = Typeface.createFromAsset(getAssets(), "fonts/712_serif.ttf");

        nameText = (TextView) findViewById(R.id.congrats);
        scoreText = (TextView) findViewById(R.id.score);
        TextView title = (TextView) findViewById(R.id.game_over);
        TextView subtitle = (TextView) findViewById(R.id.created);

        title.setTypeface(monospace);
        subtitle.setTypeface(monospace);
        nameText.setTypeface(monospace);

        backgroundMusic = MediaPlayer.create(this, R.raw.start_screen_music);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();


        Intent getConsole = getIntent();
        health = getConsole.getExtras().getInt("health");
        hunger = getConsole.getExtras().getInt("hunger");
        thirst = getConsole.getExtras().getInt("thirst");
        food = getConsole.getExtras().getInt("food");
        water = getConsole.getExtras().getInt("water");
        swordDamage = getConsole.getExtras().getInt("swordDamage");
        bullets = getConsole.getExtras().getInt("bullets");
        usersName = getConsole.getExtras().getString("UsersName");

        score = health + (hunger * 2) + (thirst * 2) + (food * 2) + (water * 2) + swordDamage + (bullets * 4);

        scoreText.setText("" + score);
        nameText.setText("Congratulations, " + usersName + "!");

        usersName = "";
        path = 0;
        health = 100;
        hunger = 10;
        thirst = 10;
        food = 3;
        water = 3;
        swordDamage = 10;
        bullets = 3;
    }

    public void onRestart(View view) {
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        float vol = 0.3f; //This will be half of the default system sound
        am.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);

        Intent toHome = new Intent(this, StartActivity.class);
        startActivity(toHome);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("USERS NAME", usersName);
        outState.putInt("PATH", path);
        outState.putInt("HEALTH", health);
        outState.putInt("HUNGER", hunger);
        outState.putInt("THIRST", thirst);
        outState.putInt("FOOD", food);
        outState.putInt("WATER", water);
        outState.putInt("SWORD DAMAGE", swordDamage);
        outState.putInt("BULLETS", bullets);

        super.onSaveInstanceState(outState);
    }

    private void saveSettings() {

        SharedPreferences settings = getSharedPreferences("User Settings", Context.MODE_PRIVATE);

        SharedPreferences.Editor sPEditor = settings.edit();

        sPEditor.putString("USERS NAME", usersName);
        sPEditor.putInt("PATH", path);
        sPEditor.putInt("HEALTH", health);
        sPEditor.putInt("HUNGER", hunger);
        sPEditor.putInt("THIRST", thirst);
        sPEditor.putInt("FOOD", food);
        sPEditor.putInt("WATER", water);
        sPEditor.putInt("SWORD DAMAGE", swordDamage);
        sPEditor.putInt("BULLETS", bullets);

        sPEditor.commit();

    }

    @Override
    protected void onStop() {
        saveSettings();
        backgroundMusic.stop();

        super.onStop();
    }


    @Override
    public void onStart() {
        backgroundMusic.start();
        super.onStart();
    }
}
