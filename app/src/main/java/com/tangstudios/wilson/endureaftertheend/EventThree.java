package com.tangstudios.wilson.endureaftertheend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Wilson on 6/28/2015.
 */
public class EventThree extends Activity {
    int health;
    int swordDamage;
    int gunDamage = 30;
    int bullets;
    int enemyHealth = 100;
    int enemySwordDamage = 10;

    String usersName;
    String status;

    TextView healthText;
    TextView swordDamageText;
    TextView bulletsText;
    TextView enemyHealthText;
    TextView enemySwordDamageText;
    LinearLayout linear;
    ScrollView scrollView;

    Button continueButton;
    Button attackButton;
    Button defendButton;
    Button shootButton;

    int msgCount = 1;
    int rand;

    boolean inBattle = false;

    MediaPlayer attackSound;
    MediaPlayer defendSound;
    MediaPlayer shootSound;
    MediaPlayer deathSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event3_layout);

        healthText = (TextView) findViewById(R.id.health);
        swordDamageText = (TextView) findViewById(R.id.sword_damage);
        enemyHealthText = (TextView) findViewById(R.id.enemy_health);
        bulletsText = (TextView) findViewById(R.id.bullets_text);
        enemySwordDamageText = (TextView) findViewById(R.id.enemy_sword_damage);
        linear = (LinearLayout) findViewById(R.id.event1_actions);
        scrollView = (ScrollView) findViewById(R.id.event_one_scroll);
        continueButton = (Button) findViewById(R.id.conButton);
        attackButton = (Button) findViewById(R.id.atk);
        defendButton = (Button) findViewById(R.id.def);
        shootButton = (Button) findViewById(R.id.shoot);

        attackSound = MediaPlayer.create(this, R.raw.sword_attack);
        defendSound = MediaPlayer.create(this, R.raw.sword_defend);
        shootSound = MediaPlayer.create(this, R.raw.gun_shoot);
        deathSound = MediaPlayer.create(this, R.raw.dying_sound);

        if (savedInstanceState != null) {
            health = savedInstanceState.getInt("EVENT 3 HEALTH");
            enemyHealth = savedInstanceState.getInt("EVENT 3 ENEMY HEALTH");
            inBattle = savedInstanceState.getBoolean("inBattle");
            bullets = savedInstanceState.getInt("EVENT 3 BULLETS");
            status = savedInstanceState.getString("STATUS");
        }

        SharedPreferences settings = getSharedPreferences("User Settings", Context.MODE_PRIVATE);

        int sPHealth = settings.getInt("EVENT 3 HEALTH", -100);
        int sPEnemyHealth = settings.getInt("EVENT 3 ENEMY HEALTH", -100);
        int sPBullets = settings.getInt("EVENT 3 BULLETS", -100);

        if (sPHealth != -100 && sPEnemyHealth != -100 && sPBullets != -100) {
            health = sPHealth;
            enemyHealth = sPEnemyHealth;
            bullets = sPBullets;
        }

        Intent getConsole = getIntent();
        health = getConsole.getExtras().getInt("health");
        swordDamage = getConsole.getExtras().getInt("swordDamage");
        usersName = getConsole.getExtras().getString("UsersName");
        bullets = getConsole.getExtras().getInt("bullets");

        healthText.setText("" + health);
        enemyHealthText.setText("" + enemyHealth);
        swordDamageText.setText("" + swordDamage);
        bulletsText.setText("" + bullets);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            scrollView.getLayoutParams().height = height / 2 - 80;
            scrollView.getLayoutParams().width = width;
            scrollView.requestLayout();
        } else {
            scrollView.getLayoutParams().height = height - height / 6;
            scrollView.getLayoutParams().width = width / 2;
            scrollView.requestLayout();
        }

        if (inBattle == true && enemyHealth > 0)
        {
            TextView[] changeOriText = new TextView[1];
            initializeTextViews(changeOriText);
            changeOriText[0].setText("--Keep fighting--");
            printTextViews(changeOriText);
        }

        if (inBattle == true && enemyHealth <= 0) {
            attackButton.setEnabled(false);
            defendButton.setEnabled(false);
            continueButton.setVisibility(View.VISIBLE);
        }
    }

    public void onAttack(View view) {
        TextView[] attackText = new TextView[2];
        initializeTextViews(attackText);

        rand = (int)(Math.random() * 5) + 1;
        if (rand == 1) {
            health-=enemySwordDamage;
            enemyHealth-=(swordDamage * 2);

            attackText[0].setText("You have taken " + enemySwordDamage + " damage");
            attackText[1].setText("Critical Hit! Enemy has taken " + (swordDamage * 2) + " damage");
        } else if (rand == 2) {
            health-=(enemySwordDamage * 2);
            enemyHealth-=swordDamage;

            attackText[0].setText("Critical Hit! You have taken " + (enemySwordDamage * 2) + " damage");
            attackText[1].setText("Enemy has taken " + swordDamage + " damage");
        } else {
            health-=enemySwordDamage;
            enemyHealth-=swordDamage;

            attackText[0].setText("You have taken " + enemySwordDamage + " damage");
            attackText[1].setText("Enemy has taken " + swordDamage + " damage");
        }

        healthText.setText("" + health);
        enemyHealthText.setText("" + enemyHealth);

        printTextViews(attackText);

        blankLine();

        attackSound.start();

        checkForWin();
        checkForGameOver();
        scroll();
    }

    public void onDefend(View view) {
        TextView[] defendText = new TextView[2];
        initializeTextViews(defendText);

        rand = (int)(Math.random() * 2) + 1;
        if (rand == 1) {
            health-=(enemySwordDamage * 2);

            defendText[0].setText("Defense Failed! You have taken " + (enemySwordDamage * 2) + " damage");
            defendText[1].setText("Enemy has taken 0 damage");

            attackSound.start();
        } else {
            enemyHealth-=(swordDamage * 2);

            defendText[0].setText("Defense Successful! You have taken 0 damage");
            defendText[1].setText("Enemy has taken " + (swordDamage * 2) + " damage");

            defendSound.start();
        }

        healthText.setText("" + health);
        enemyHealthText.setText("" + enemyHealth);

        printTextViews(defendText);

        blankLine();

        checkForWin();
        checkForGameOver();
        scroll();
    }

    public void onShoot(View view) {
        if (bullets > 0) {
            TextView[] shootText = new TextView[2];
            initializeTextViews(shootText);

            bullets--;
            rand = (int) (Math.random() * 5) + 1;

            if (rand == 1) {
                health -= (enemySwordDamage * 2);

                shootText[0].setText("Shot missed! You have taken " + (enemySwordDamage * 2) + " damage");
                shootText[1].setText("Enemy has taken 0 damage");
            } else {
                enemyHealth -= gunDamage;

                shootText[0].setText("Shots fired! You have taken 0 damage");
                shootText[1].setText("Enemy has taken 30 damage");

                shootSound.start();
            }

            healthText.setText("" + health);
            enemyHealthText.setText("" + enemyHealth);
            bulletsText.setText("" + bullets);

            printTextViews(shootText);
            blankLine();

            shootSound.start();

            checkForWin();
            checkForGameOver();
            scroll();
        } else {
            TextView[] noBullets = new TextView[1];
            initializeTextViews(noBullets);
            noBullets[0].setText("No bullets!");

            printTextViews(noBullets);
            blankLine();
            scroll();
        }
    }

    public void onContinue(View view) {
        String backHealth = "" + health;
        String backBullets = "" + bullets;

        if (status.equals("Game Over")) {
            health = 100;
            enemyHealth = 100;

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

            editor.commit();

            Intent gameOver = new Intent(this, StartActivity.class);
            gameOver.putExtra("status", status);
            setResult(RESULT_OK, gameOver);
            finish();
        }

        if (status.equals("Event Three Complete")) {
            TextView[] messages = new TextView[3];
            String[] messagesText = new String[3];
            initializeTextViews(messages);

            if (msgCount == 1) {
                messagesText[0] = "You: *panting*";
                messagesText[1] = "Aila: " + usersName + ", are you alright?";
                messagesText[2] = "*You: Yeah, I'm fine.";
            } else if (msgCount == 2) {
                messagesText[0] = "*You and Aila look at the dead body*";
                messagesText[1] = "Aila: What.. do we do about his son?";
                messagesText[2] = "You: I don't know..";
            } else if (msgCount == 3) {
                messagesText[0] = "*The son looks frightened*";
                messagesText[1] = "You: We should go up to him..";
                messagesText[2] = "Aila: Are you sure?";
            } else if (msgCount == 4) {
                messagesText[0] = "You: Yeah, it'll be fine.";

                continueButton.setText("Approach boy");
            } else {
                enemyHealth = 100;

                Intent goingBack = new Intent();
                goingBack.putExtra("newHealth2", backHealth);
                goingBack.putExtra("bullets", backBullets);
                goingBack.putExtra("status", status);
                setResult(RESULT_OK, goingBack);

                finish();
            }

            setTextViewText(messages, messagesText);
            printTextViews(messages);
            blankLine();
            msgCount++;

            AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
            float vol = 0.1f; //This will be half of the default system sound
            am.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);

            scroll();
        }
    }

    public void checkForGameOver() {
        if (health <= 0) {
            health = 0;
            healthText.setText("" + health);

            continueButton.setVisibility(View.VISIBLE);
            attackButton.setVisibility(View.INVISIBLE);
            defendButton.setVisibility(View.INVISIBLE);
            shootButton.setVisibility(View.INVISIBLE);
            continueButton.setText("Game Over.");

            status = "Game Over";

            deathSound.start();
        }
    }

    public void checkForWin() {
        if (enemyHealth <= 0 && health >= 0) {
            enemyHealth = 0;
            enemyHealthText.setText("" + enemyHealth);

            TextView[] win = new TextView[1];
            initializeTextViews(win);
            win[0].setText("He's down...");
            printTextViews(win);
            continueButton.setVisibility(View.VISIBLE);
            attackButton.setVisibility(View.INVISIBLE);
            defendButton.setVisibility(View.INVISIBLE);
            shootButton.setVisibility(View.INVISIBLE);

            continueButton.setText("Next");

            status = "Event Three Complete";
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt("EVENT 3 HEALTH", health);
        outState.putInt("EVENT 3 ENEMY HEALTH", enemyHealth);
        outState.putInt("EVENT 3 BULLETS", bullets);
        outState.putString("STATUS", status);
        outState.putBoolean("inBattle", true);

        super.onSaveInstanceState(outState);
    }

    private void saveSettings() {

        SharedPreferences settings = getSharedPreferences("User Settings", Context.MODE_PRIVATE);

        SharedPreferences.Editor sPEditor = settings.edit();

        sPEditor.putInt("EVENT 3 HEALTH", health);
        sPEditor.putInt("EVENT 3 ENEMY HEALTH", enemyHealth);
        sPEditor.putInt("EVENT 3 BULLETS", bullets);

        sPEditor.commit();

    }

    @Override
    protected void onStop() {

        saveSettings();

        super.onStop();
    }

    public void initializeTextViews(TextView[] textViews) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i] = new TextView(this);
            textViews[i].setTextColor(Color.BLACK);
            textViews[i].setTypeface(Typeface.create("monospace", Typeface.NORMAL));
        }
    }

    public void setTextViewText(TextView[] textViews, String[] text) {
        for (int i = 0; i < text.length; i++) {
            textViews[i].setText(text[i]);
        }
    }

    public void printTextViews(TextView[] textViews) {
        for (int i = 0; i < textViews.length; i++) {
            if (textViews[i].getText().toString().length() != 0 ) ((LinearLayout) linear).addView(textViews[i]);
        }
    }

    public void blankLine() {
        TextView blank = new TextView(this);
        blank.setText("");
        linear.addView(blank);
    }

    public void scroll() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
