package com.tangstudios.wilson.endureaftertheend;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Wilson on 2/9/2015.
 */
public class EnterNameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.enter_name_layout);
    }

    public void onGo(View view) {
        //Get Name
        EditText usersNameET = (EditText) findViewById(R.id.users_name);

        String usersName = usersNameET.getText().toString();

        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        float vol = 0.3f; //This will be half of the default system sound
        am.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);

        //to Console Screen

        if (usersName.equals("")) {
            Toast.makeText(this, "Please Enter a Name", Toast.LENGTH_LONG).show();
        } else {
            Intent getNameScreenIntent = new Intent(this,
                    Console.class);

            final int result = 1;

            getNameScreenIntent.putExtra("UsersName", usersName);

            startActivityForResult(getNameScreenIntent, result);

            finish();
        }
    }
}
