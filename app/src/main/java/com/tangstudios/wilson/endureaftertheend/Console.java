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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Wilson on 6/27/2015.
 */
public class Console extends Activity {

    int path = 0;
    int health;
    int hunger;
    int food;
    int thirst;
    int water;
    int swordDamage;
    int bullets;
    int reduceHealth;
    int randomEvent;
    int messageCount;

    String usersName;
    String backHealth;
    String backBullets;
    String status;

    TextView healthText;
    TextView hungerText;
    TextView foodText;
    TextView thirstText;
    TextView waterText;
    TextView swordDamageText;
    TextView gunDamageText;
    TextView bulletsText;
    TextView gunDamageTextDisplay;
    TextView bulletsTextDisplay;

    LinearLayout linear;
    ScrollView scrollView;

    Button forward;
    Button eat;
    Button drink;
    Button special;

    ArrayList<String> consoleMessages = new ArrayList<>();

    MediaPlayer walkingSound;
    MediaPlayer eatingSound;
    MediaPlayer drinkingSound;
    MediaPlayer runningSound;
    MediaPlayer deathSound;
    MediaPlayer equipSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.console_layout);

        healthText = (TextView) findViewById(R.id.health); //Declare... EVERYTHING!!!!
        hungerText = (TextView) findViewById(R.id.hunger);
        foodText = (TextView) findViewById(R.id.food);
        thirstText = (TextView) findViewById(R.id.thirst);
        waterText = (TextView) findViewById(R.id.water);
        swordDamageText = (TextView) findViewById(R.id.sword_damage);
        gunDamageText = (TextView) findViewById(R.id.gun_damage);
        bulletsText = (TextView) findViewById(R.id.bullets);
        gunDamageTextDisplay = (TextView) findViewById(R.id.gun_display);
        bulletsTextDisplay = (TextView) findViewById(R.id.gun_display_2);

        special = (Button) findViewById(R.id.special);
        forward = (Button) findViewById(R.id.forward);
        eat = (Button) findViewById(R.id.eat);
        drink = (Button) findViewById(R.id.drink);

        linear = (LinearLayout) findViewById(R.id.linear);
        scrollView = (ScrollView) findViewById(R.id.console_scroll);

        walkingSound = MediaPlayer.create(this, R.raw.walking_sound);
        eatingSound = MediaPlayer.create(this, R.raw.eat_sound);
        drinkingSound = MediaPlayer.create(this, R.raw.drink_sound);
        runningSound = MediaPlayer.create(this, R.raw.running_sound);
        deathSound = MediaPlayer.create(this, R.raw.dying_sound);
        equipSound = MediaPlayer.create(this, R.raw.equip_sound); //Now that's... A LOT of a variables you got there.

        //Saved data
        if (savedInstanceState != null) {
            usersName = savedInstanceState.getString("USERS NAME");
            path = savedInstanceState.getInt("PATH");

            health = savedInstanceState.getInt("HEALTH");
            hunger = savedInstanceState.getInt("HUNGER");
            thirst = savedInstanceState.getInt("THIRST");
            food = savedInstanceState.getInt("FOOD");
            water = savedInstanceState.getInt("WATER");
            swordDamage = savedInstanceState.getInt("SWORD DAMAGE");
            bullets = savedInstanceState.getInt("BULLETS");
        } else {
            SharedPreferences settings = getSharedPreferences("User Settings", Context.MODE_PRIVATE);
            String sPUsersName = settings.getString("USERS NAME", "EMPTY");
            if (!sPUsersName.equals("EMPTY")) usersName = sPUsersName;
        }

        SharedPreferences settings = getSharedPreferences("User Settings", Context.MODE_PRIVATE);

        String sPUsersName = settings.getString("USERS NAME", "EMPTY");
        int sPPath = settings.getInt("PATH", -100);
        int sPHealth = settings.getInt("HEALTH", -100);
        int sPHunger = settings.getInt("HUNGER", -100);
        int sPThirst = settings.getInt("THIRST", -100);
        int sPFood = settings.getInt("FOOD", -100);
        int sPWater = settings.getInt("WATER", -100);
        int sPSwordDamage = settings.getInt("SWORD DAMAGE", -100);
        int sPBullets = settings.getInt("BULLETS", -100);

        if (!sPUsersName.equals("EMPTY")) usersName = sPUsersName;

        if (sPPath != -100 && sPHealth != -100 && sPHunger != -100 && sPThirst != -100 &&
                sPFood != -100 && sPWater != -100 && sPSwordDamage != -100 && sPBullets != -100) {
            path = sPPath;
            health = sPHealth;
            hunger = sPHunger;
            thirst = sPThirst;
            food = sPFood;
            water = sPWater;
            swordDamage = sPSwordDamage;
            bullets = sPBullets;

            healthText.setText("" + health);
            hungerText.setText("" + hunger);
            thirstText.setText("" + thirst);
            foodText.setText("" + food);
            waterText.setText("" + water);
            swordDamageText.setText("" + swordDamage);
            bulletsText.setText("" + bullets);
        }

        if (path == 0) {
            Intent getName = getIntent();
            usersName = getName.getExtras().getString("UsersName");

            health = 100;
            hunger = 10;
            food = 2;
            thirst = 10;
            water = 2;
            swordDamage = 10;
            bullets = 3;

            TextView[] startTextViews = new TextView[5]; //First messages
            String[] startTextViewsText = {"You wake up in a dead forest.",
                    "Everywhere around you is a barren wasteland.",
                    "You have some scraggy clothes, some food, some water, and a sword.",
                    "There is no one and nothing around you.",
                    "You are lost."};
            initializeTextViews(startTextViews);
            setTextViewText(startTextViews, startTextViewsText);
            printTextViews(startTextViews);

            addToConsole(startTextViewsText);
        }

        if (path > 0) {
            TextView[] cont = new TextView[1];
            initializeTextViews(cont);
            cont[0].setText("--Continue on--");
            printTextViews(cont);
        }

        if (path == 10 || path == 20 || path == 40) {
            path--;
        }

        if (path >= 27) {
            gunDamageText.setVisibility(View.VISIBLE);
            bulletsText.setVisibility(View.VISIBLE);
            gunDamageTextDisplay.setVisibility(View.VISIBLE);
            bulletsTextDisplay.setVisibility(View.VISIBLE);
        }

        if (path >= 55 && path <= 60) {
            path = 54;
        }

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
            scrollView.getLayoutParams().width = width / 2- width / 32;
            scrollView.requestLayout();
        }

        //For Debugging and Testing
        if (usersName.equals("ricerice10")) {
            path = 9;
        }
        if (usersName.equals("ricerice20")) {
            path = 19;
        }
        if (usersName.equals("ricerice27")) {
            path = 26;
        }
        if (usersName.equals("ricerice40")) {
            path = 39;
        }
        if (usersName.equals("ricerice55")) {
            path = 54;
        }
        if (usersName.equals("ricerice60")) {
            path = 59;
        }
        if (usersName.equals("ricerice69")) {
            path = 68;
        }
        if (usersName.equals("ricestrong")) {
            swordDamage = 100;
        }
    }

    public void onForward(View view) {
        if (path >= 55 && path < 60) {
            onLeft();
        } else {
            blankLine();

            path++; //keeps the path

            reduceHealth = (int) (Math.random() * 5) + 8; //Survival aspect of game
            health -= reduceHealth;
            hunger--;
            thirst--;

            TextView[] forwardMessages = new TextView[8]; //Formatting the messages
            String[] forwardMessagesText = {"-" + reduceHealth + " health: walking", "-1 hunger: walking",
                    "-1 thirst: walking", "", "", "", "", ""};
            initializeTextViews(forwardMessages);

            if (hunger <= 0) { //Subtract health if hunger or thirst is <= 0
                health -= 5;
                forwardMessagesText[4] = "-5 health: hungry";
            }
            if (thirst <= 0) {
                health -= 5;
                forwardMessagesText[5] = "-5 health: thirsty";
            }

            randomEvent = (int) (Math.random() * 10) + 1; //Random Events

            if (randomEvent == 1) {
                food += 2;
                forwardMessagesText[3] = "+2 food: found a big can of canned meat";
            } else if (randomEvent == 2) {
                food++;
                forwardMessagesText[3] = "+1 food: found a can of canned meat";
            } else if (randomEvent == 3) {
                water += 2;
                forwardMessagesText[3] = "+2 water: found a big bottle of water";
            } else if (randomEvent == 4) {
                water++;
                forwardMessagesText[3] = "+1 water: found bottled water";
            } else if (randomEvent == 5) {
                health = 100;
                forwardMessagesText[3] = "Full Health: found a med-kit";
            } else if (randomEvent == 6 && swordDamage < 30) {
                swordDamage += 10;
                forwardMessagesText[3] = "+10 sword damage: new, sharper sword";
            } else if (randomEvent == 7) {
                health += 20;
                if (health > 100) health = 100;
                forwardMessagesText[3] = "+20 health: found band-aids";
            } else if (randomEvent == 8) {
                food++;
                forwardMessagesText[3] = "+1 food: found a can of canned meat";
            } else if (randomEvent == 9) {
                water++;
                forwardMessagesText[3] = "+1 water: found bottled water";
            } else if (randomEvent == 10 && path > 27 && bullets < 5) {
                bullets++;
                forwardMessagesText[3] = "+1 bullet: found an old bullet on the ground";
            }

            if (food > 5) { //Check if there's too much food or water to carry
                forwardMessagesText[6] = "-" + (food - 5) + " food: too much food to carry";
                food = 5;
            }
            if (water > 5) {
                forwardMessagesText[7] = "-" + (water - 5) + " water: too much water to carry";
                water = 5;
            }

            if (health < 0) health = 0; //So that there are no negatives
            if (hunger < 0) hunger = 0;
            if (thirst < 0) thirst = 0;

            healthText.setText("" + health); //Sets all the components
            hungerText.setText("" + hunger);
            thirstText.setText("" + thirst);
            foodText.setText("" + food);
            waterText.setText("" + water);
            swordDamageText.setText("" + swordDamage);
            bulletsText.setText("" + bullets);


            setTextViewText(forwardMessages, forwardMessagesText); //Sets and Prints TextViews
            printTextViews(forwardMessages);

            consoleMessages.add(" ");
            addToConsole(forwardMessagesText);

            checkForEvents();

            if (health <= 0) //Game over
            {
                health = 0;
                healthText.setText("" + health);

                forward.setVisibility(View.INVISIBLE);
                eat.setVisibility(View.INVISIBLE);
                drink.setVisibility(View.INVISIBLE);
                special.setVisibility(View.VISIBLE);
                special.setText("Game Over.");

                status = "Game Over";

                usersName = "";
                path = 0;
                health = 100;
                hunger = 10;
                thirst = 10;
                food = 3;
                water = 3;
                swordDamage = 10;
                bullets = 3;

                deathSound.start();
            }

            walkingSound.start();

            if (path == 11)
                special.setVisibility(View.INVISIBLE); //Set after Event One if player didn't cook Cyrus


            scroll(); //scrolls to the bottom of the scrollview
        }
    }

    public void onEat(View view) {
        if (path >= 55 && path < 60) {
            onForward();
        } else {
            blankLine();

            TextView[] eatMessages = new TextView[3]; //Starts the messages
            String[] eatMessagesText = new String[3];
            initializeTextViews(eatMessages);

            if (food > 0) {
                hunger += 5; //Calculations for eat
                health += 10;
                food--;

                if (health > 100) health = 100; //Nothing above the limit
                if (hunger > 10) hunger = 10;

                eatMessagesText[0] = "+5 hunger: ate food";
                eatMessagesText[1] = "+10 health: re-energized";
                eatMessagesText[2] = "-1 food: ate food";

                eatingSound.start();
            } else {
                eatMessagesText[0] = "No food!";
            }

            healthText.setText("" + health); //Sets the components
            hungerText.setText("" + hunger);
            foodText.setText("" + food);

            setTextViewText(eatMessages, eatMessagesText); //Sets and prints the messages
            printTextViews(eatMessages);

            //scrolls to the bottom of the scrollview
            scroll();
        }
    }

    public void onDrink(View view) {
        if (path >= 55 && path < 60) {
            onRight();
        } else {
            blankLine();

            TextView[] drinkMessages = new TextView[3]; //Starts the messages
            String[] drinkMessagesText = new String[3];
            initializeTextViews(drinkMessages);

            if (water > 0) {
                thirst += 5; //Calculations for drink
                health += 10;
                water--;

                if (health > 100) health = 100; //Nothing above the limit
                if (thirst > 10) thirst = 10;

                drinkMessagesText[0] = "+5 thirst: drank water";
                drinkMessagesText[1] = "+10 health: re-energized";
                drinkMessagesText[2] = "-1 water: drank water";

                drinkingSound.start();
            } else {
                drinkMessagesText[0] = "No water!";
            }

            healthText.setText("" + health); //Sets the components
            thirstText.setText("" + thirst);
            waterText.setText("" + water);

            setTextViewText(drinkMessages, drinkMessagesText); //Sets and prints the messages
            printTextViews(drinkMessages);

            //scrolls to the bottom of the scrollview
            scroll();
        }
    }

    public void onSpecial(View view) {                                                      //Only status if's go in onSpecial
        if (status.equals("Game Over")) {
            Intent gameOver = new Intent(this, GameOver.class);
            startActivity(gameOver);
            finish();
        }

        if (status.equals("Event One Start")) {
            blankLine();

            if (messageCount == 0) {
                TextView[] text = new TextView[3];
                String[] textforText = {"*Maybe he'll help me...*", "You: Hey!", "No reply..."};
                initializeTextViews(text);
                setTextViewText(text, textforText);
                printTextViews(text);
            } else if (messageCount == 1) {
                TextView[] text = new TextView[2];
                String[] textforText = {"He has a machete in his hand", "He raises it up and charges at you."};
                initializeTextViews(text);
                setTextViewText(text, textforText);
                printTextViews(text);
                special.setText("Equip Sword");
            } else if (messageCount == 2) {
                equipSound.start();

                Intent toEventOne = new Intent(this, EventOne.class);

                final int result = 1;

                toEventOne.putExtra("health", health);
                toEventOne.putExtra("swordDamage", swordDamage);
                toEventOne.putExtra("UsersName", usersName);

                startActivityForResult(toEventOne, result);
            }

            messageCount++;
            scroll();
        }

        if (status.equals("Event One Complete")) {
            food += 3;
            foodText.setText("" + food);
            special.setVisibility(View.INVISIBLE);
            status = "";

            TextView[] cookCyrus = new TextView[2];
            String[] cookCyrusText = {"+3 food: cooked Cyrus", "Continue forward"};
            initializeTextViews(cookCyrus);
            setTextViewText(cookCyrus, cookCyrusText);
            printTextViews(cookCyrus);

            scroll();
        }

        if (status.equals("Event Two Start")) {
            blankLine();
            TextView[] eventTwoStartMessages = new TextView[4];
            initializeTextViews(eventTwoStartMessages);
            String[] eventTwoStartMessagesText = new String[4];

            if (messageCount == 0) {
                eventTwoStartMessagesText[0] = "*You walk into the cabin.*";
                eventTwoStartMessagesText[1] = "*You see a little girl tied to a chair, blindfolded.*";
                eventTwoStartMessagesText[2] = "*Her screams were muffled through a mask.*";
                eventTwoStartMessagesText[3] = "*No one else is in the room.*";

                special.setText("Next");
            } else if (messageCount == 1) {
                eventTwoStartMessagesText[0] = "*You run inside and quickly untie the girl.*";
            } else if (messageCount == 2) {
                eventTwoStartMessagesText[0] = "You: Shhhhhh, it's alright. I won't hurt you. I'm gonna get you out.";
                eventTwoStartMessagesText[1] = "Girl: *looks at you supiciously*";
                eventTwoStartMessagesText[2] = "You: I'm " + usersName + ". You?";
            } else if (messageCount == 3) {
                eventTwoStartMessagesText[0] = "Girl: I'm... Aila..";
                eventTwoStartMessagesText[1] = "You: Aila.. That's a beautiful name.";
                eventTwoStartMessagesText[2] = "Aila: *weak chuckle*";
            } else if (messageCount == 4) {
                eventTwoStartMessagesText[0] = "You: Are you alright? What happened?";
                eventTwoStartMessagesText[1] = "Aila: A big man.. took me..";
                eventTwoStartMessagesText[2] = "You: ...Let's get out of here quickly, Aila.";
                eventTwoStartMessagesText[3] = "Aila: O..kay...";
            } else if (messageCount == 5) {
                eventTwoStartMessagesText[0] = "*A bulky, bearded man storms into the cabin*";
            } else if (messageCount == 6) {
                eventTwoStartMessagesText[0] = "Man: WHAT ARE YOU DOING TO MY LADY?!";
                eventTwoStartMessagesText[1] = "You: I'm freeing her.";
                eventTwoStartMessagesText[2] = "Man: OH NO YOU'RE NOT. SHE'S STAYING HERE!";
            } else if (messageCount == 7) {
                eventTwoStartMessagesText[0] = "You: And you're going to stop me??";
                eventTwoStartMessagesText[1] = "Man: THAT'S RIGHT I AM.";
                eventTwoStartMessagesText[2] = "Man: I AM BANJO, THE MIGHTIEST MAN IN THE WORLD!";
            } else if (messageCount == 8) {
                eventTwoStartMessagesText[0] = "*Banjo takes out a big axe and starts swinging it around.*";
            } else if (messageCount == 9) {
                eventTwoStartMessagesText[0] = "You: *whispers to Aila* It'll be alright, Aila...";
                eventTwoStartMessagesText[1] = "Banjo: GET READY, LITTLE MAN. 'CUZ THAT LITTLE LADY'S MINE!!";
            } else if (messageCount == 10) {
                eventTwoStartMessagesText[0] = "*Banjo charges at you.*";

                special.setText("Equip Sword");
            } else {
                equipSound.start();

                Intent toEventOne = new Intent(this, EventTwo.class);

                final int result = 1;

                toEventOne.putExtra("health", health);
                toEventOne.putExtra("swordDamage", swordDamage);
                toEventOne.putExtra("UsersName", usersName);

                startActivityForResult(toEventOne, result);
            }

            setTextViewText(eventTwoStartMessages, eventTwoStartMessagesText);
            printTextViews(eventTwoStartMessages);

            messageCount++;
            scroll();
        }

        if (status.equals("Rainstorm")) {
            blankLine();
            TextView[] eventTwoStartMessages = new TextView[4];
            initializeTextViews(eventTwoStartMessages);
            String[] eventTwoStartMessagesText = new String[4];

            if (messageCount == 0) {
                eventTwoStartMessagesText[0] = "You: Aila, let's go find some shelter.";
                eventTwoStartMessagesText[1] = "Aila: Okay..";

                special.setText("Next");
            } else if (messageCount == 1) {
                eventTwoStartMessagesText[0] = "*You and Aila find a small cave*";
                eventTwoStartMessagesText[1] = "*You find some firewood and make a small campfire*";
            }  else if (messageCount == 2) {
                eventTwoStartMessagesText[0] = "You: Are you cold, Aila?";
                eventTwoStartMessagesText[1] = "Aila: I'm fine..";
                eventTwoStartMessagesText[2] = "You: Here, take some food.";

                special.setText("Give Food");
            } else if (messageCount == 3) {
                eventTwoStartMessagesText[0] = "Aila: Thank you..";
                eventTwoStartMessagesText[1] = " ";
                eventTwoStartMessagesText[2] = "-1 food: gave food to Aila";

                if (food > 0) food--;
                foodText.setText("" + food);
                special.setText("Next");
            } else if (messageCount == 4) {
                eventTwoStartMessagesText[0] = "You: So do you.. remember life before this?";
                eventTwoStartMessagesText[1] = "Aila: Not really..";
                eventTwoStartMessagesText[2] = "Aila: Whenever I try to think back..";
                eventTwoStartMessagesText[3] = "Aila: I get bad memories..";
            } else if (messageCount == 5) {
                eventTwoStartMessagesText[0] = "Aila: I don't remember anything..";
                eventTwoStartMessagesText[1] = "Aila: All I remember is when that big man took me..";
                eventTwoStartMessagesText[2] = "Aila: And when you rescued me..";
            } else if (messageCount == 6) {
                eventTwoStartMessagesText[0] = "You: I don't remember anything either.";
                eventTwoStartMessagesText[1] = "You: I can remember a few flashbacks, but..";
                eventTwoStartMessagesText[2] = "You: All the faces, everything.. is blurry to me..";
            } else if (messageCount == 7) {
                eventTwoStartMessagesText[0] = "You: I think I had a family..";
                eventTwoStartMessagesText[1] = "You: Do you remember your family?";
                eventTwoStartMessagesText[2] = "Aila: No..";
            } else if (messageCount == 8) {
                eventTwoStartMessagesText[0] = "*You and Aila lay down and fall asleep*";
            } else if (messageCount == 9) {
                eventTwoStartMessagesText[0] = "*When you and Aila wake up, the storm is gone*";
            } else if (messageCount == 10) {
                eventTwoStartMessagesText[0] = "You: Aila, you see that mountain over there?";
                eventTwoStartMessagesText[1] = "Aila: Yeah..";
            } else if (messageCount == 11) {
                eventTwoStartMessagesText[0] = "You: I heard that there's a safe camp over that mountain";
                eventTwoStartMessagesText[1] = "You: I think that's where we should go";
                eventTwoStartMessagesText[2] = "Aila: Okay..";

                special.setText("Continue Forward");
            } else {
                forward.setEnabled(true);
                special.setVisibility(View.INVISIBLE);
            }

            setTextViewText(eventTwoStartMessages, eventTwoStartMessagesText);
            printTextViews(eventTwoStartMessages);

            messageCount++;
            scroll();
        }
        
        if (status.equals("Event Three Start")) {
            blankLine();
            TextView[] eventThreeStartMessages = new TextView[4];
            initializeTextViews(eventThreeStartMessages);
            String[] eventThreeStartMessagesText = new String[4];

            if (messageCount == 0) {
                eventThreeStartMessagesText[0] = "You: I don't know..";
                eventThreeStartMessagesText[1] = "You: Let's see what they do..";
            } else if (messageCount == 1) {
                eventThreeStartMessagesText[0] = "Aila: I'm scared..";
                eventThreeStartMessagesText[1] = "You: It's alright, I'll protect you.";
            } else if (messageCount == 2) {
                eventThreeStartMessagesText[0] = "*The father starts walking closer to you and Aila*";
                eventThreeStartMessagesText[1] = "You: Hey! I wouldn't get any closer!";
                eventThreeStartMessagesText[2] = "*He starts charging at you with a knife in his hand*";

                eat.setEnabled(false);
                drink.setEnabled(false);
            } else if (messageCount == 3) {
                eventThreeStartMessagesText[0] = "Aila: " + usersName + "!";
                eventThreeStartMessagesText[1] = "You: Aila, stay back.";
                eventThreeStartMessagesText[2] = "You: I won't let him lay a hand on you.";
                
                special.setText("Equip Weapons");
            } else {
                equipSound.start();

                Intent toEventThree = new Intent(this, EventThree.class);

                final int result = 1;

                toEventThree.putExtra("health", health);
                toEventThree.putExtra("swordDamage", swordDamage);
                toEventThree.putExtra("bullets", bullets);
                toEventThree.putExtra("UsersName", usersName);

                startActivityForResult(toEventThree, result);
            }
            
            setTextViewText(eventThreeStartMessages, eventThreeStartMessagesText);
            printTextViews(eventThreeStartMessages);

            messageCount++;
            scroll();
        }

        if (status.equals("Event Three Complete")) {
            blankLine();
            TextView[] eventThreeCompleteMessages = new TextView[5];
            initializeTextViews(eventThreeCompleteMessages);
            String[] eventThreeCompleteMessagesText = new String[5];

            if (messageCount == 0) {
                eventThreeCompleteMessagesText[0] = "You: Hey.. Are you okay?";
                eventThreeCompleteMessagesText[1] = "*The boy doesn't respond*";
                eventThreeCompleteMessagesText[2] = "*He is just standing there.. Traumatized..*";
            } else if (messageCount == 1) {
                eventThreeCompleteMessagesText[0] = "You: It's alright, we won't hurt you.";
                eventThreeCompleteMessagesText[1] = "Aila: Do we.. help him?";
                eventThreeCompleteMessagesText[2] = "*There is a moment of silence*";
            } else if (messageCount == 2) {
                eventThreeCompleteMessagesText[0] = "You: Hey, here's some food and water.";
                eventThreeCompleteMessagesText[1] = "*You put down food and water for the boy*";
                eventThreeCompleteMessagesText[2] = " ";
                eventThreeCompleteMessagesText[3] = "-1 food: gave food to boy";
                eventThreeCompleteMessagesText[4] = "-1 water: gave water to boy";

                if (food > 0) food--;
                if (water > 0) water--;
                foodText.setText("" + food);
                waterText.setText("" + water);
            } else if (messageCount == 3) {
                eventThreeCompleteMessagesText[0] = "You: Come on, Aila. Let's go.";
                eventThreeCompleteMessagesText[1] = "Aila: Wait, but..";
            } else if (messageCount == 4) {
                eventThreeCompleteMessagesText[0] = "You: We should go, Aila.";
                eventThreeCompleteMessagesText[1] = "Aila: Okay..";
            } else if (messageCount == 5) {
                eventThreeCompleteMessagesText[0] = "*As you and Aila walk away*";
                eventThreeCompleteMessagesText[1] = "*The boy walks to his father's dead body*";
                eventThreeCompleteMessagesText[2] = "*He kneels down and just sits there*";

                special.setText("Continue forward");
            } else {
                forward.setEnabled(true);
                eat.setEnabled(true);
                drink.setEnabled(true);
                special.setVisibility(View.INVISIBLE);
            }

            setTextViewText(eventThreeCompleteMessages, eventThreeCompleteMessagesText);
            printTextViews(eventThreeCompleteMessages);

            messageCount++;
            scroll();
        }

        if (status.equals("Post-Event Three Talk")) {
            blankLine();
            TextView[] postEventThreeStartMessages = new TextView[5];
            initializeTextViews(postEventThreeStartMessages);
            String[] postEventThreeStartMessagesText = new String[5];

            if (messageCount == 0) {
                postEventThreeStartMessagesText[0] = "You: What's wrong, Aila?";
                postEventThreeStartMessagesText[1] = "Aila: Why didn't we help him?";
                postEventThreeStartMessagesText[2] = "Aila: He didn't do anything wrong.";
            } else if (messageCount == 1) {
                postEventThreeStartMessagesText[0] = "You: Aila..";
                postEventThreeStartMessagesText[1] = "Aila: We just.. left him there";
                postEventThreeStartMessagesText[2] = "Aila: To die...";
            } else if (messageCount == 2) {
                postEventThreeStartMessagesText[0] = "*You don't know what to say*";
                postEventThreeStartMessagesText[1] = "*You both just stand there, silently*";
            } else if (messageCount == 3) {
                postEventThreeStartMessagesText[0] = "You: Aila, you should know..";
                postEventThreeStartMessagesText[1] = "You: We can't trust anyone.";
                postEventThreeStartMessagesText[2] = "You: We can't take that risk.";
            } else if (messageCount == 4) {
                postEventThreeStartMessagesText[0] = "Aila: But he was just a kid..";
                postEventThreeStartMessagesText[1] = "You: Honestly..";
                postEventThreeStartMessagesText[2] = "You: I don't know if I made the right decision";
            } else if (messageCount == 5) {
                postEventThreeStartMessagesText[0] = "Aila: Is he.. going to die?";
                postEventThreeStartMessagesText[1] = "You: I don't know..";
            } else if (messageCount == 6) {
                postEventThreeStartMessagesText[0] = "You: We should.. keep going.";
                postEventThreeStartMessagesText[1] = "Aila: Okay..";

                special.setText("Continue forward");
            } else {
                forward.setEnabled(true);
                eat.setEnabled(true);
                drink.setEnabled(true);
                special.setVisibility(View.INVISIBLE);
            }

            setTextViewText(postEventThreeStartMessages, postEventThreeStartMessagesText);
            printTextViews(postEventThreeStartMessages);

            messageCount++;
            scroll();
        }
        
        if (status.equals("Approach City")) {
            blankLine();
            TextView[] approachCityMessages = new TextView[5];
            initializeTextViews(approachCityMessages);
            String[] approachCityMessagesText = new String[5];

            if (messageCount == 0) {
                approachCityMessagesText[0] = "*You run to the nearest boulder*";
                approachCityMessagesText[1] = "You: AILA, HIDE!!!!";
                approachCityMessagesText[2] = "You: AILA?!?!";

                special.setText("Next");
            } else if (messageCount == 1) {
                approachCityMessagesText[0] = "*You don't see Aila*";
                approachCityMessagesText[1] = "You: AILA, WHERE ARE YOU?!";
                approachCityMessagesText[2] = "*Sniper bullets continue to fly near you*";
            } else if (messageCount == 2) {
                approachCityMessagesText[0] = "*You can't find Aila anywhere around you*";
                approachCityMessagesText[1] = "You: AILA!!!";
            } else {
                approachCityMessagesText[0] = "You: I need to find cover...";
                approachCityMessagesText[1] = "You: But I can't get shot..";
                approachCityMessagesText[2] = "*Tap to run in the direction you want*";
                approachCityMessagesText[3] = "*Don't get shot*";

                forward.setEnabled(true);
                eat.setEnabled(true);
                drink.setEnabled(true);

                forward.setText("Left");
                eat.setText("Forward");
                drink.setText("Right");

                special.setVisibility(View.INVISIBLE);
            }

            setTextViewText(approachCityMessages, approachCityMessagesText);
            printTextViews(approachCityMessages);

            messageCount++;
            scroll();
        }
        
        if (status.equals("In House")) {
            blankLine();
            TextView[] inHouseMessages = new TextView[5];
            initializeTextViews(inHouseMessages);
            String[] inHouseMessagesText = new String[5];

            if (messageCount == 0) {
                inHouseMessagesText[0] = "You: *panting* I'm safe.. for now..";
                inHouseMessagesText[1] = "You: Where did Aila go..";
            } else if (messageCount == 1) {
                inHouseMessagesText[0] = "*You look around the house*";
                inHouseMessagesText[1] = "*You are trying to find supplies, guns, anything*";
            } else if (messageCount == 2) {
                inHouseMessagesText[0] = "*Inside a file cabinet, you find a bunch of old newspapers*";
                inHouseMessagesText[1] = "You: What is this?..";
            } else if (messageCount == 3){
                inHouseMessagesText[0] = "*You read the headlines:*";
                inHouseMessagesText[1] = "*TRINITY CORP NUCLEAR BOMB*";
                inHouseMessagesText[2] = "*TRINITY CORP BUYS OUT TOP COMPANIES*";
                inHouseMessagesText[3] = "*TRINITY CORP MONOPOLY*";
                inHouseMessagesText[4] = "*TRINITY CORP TAKEOVER*";
            } else if (messageCount == 4){
                inHouseMessagesText[0] = "You: Trinity Corp... I worked there?..";
                inHouseMessagesText[1] = "You: Cyrus...";
                inHouseMessagesText[2] = "You: What does all this mean?";
            } else if (messageCount == 5){
                inHouseMessagesText[0] = "*You put down the newspapers and continue to find supplies*";
                inHouseMessagesText[1] = "You: I have to get out of here..";
                inHouseMessagesText[2] = "You: But I need to find Aila first..";
            } else if (messageCount == 6){
                inHouseMessagesText[0] = "*voice yells quietly from back door* " + usersName + "!";
                inHouseMessagesText[1] = "You: Aila?..";
                inHouseMessagesText[2] = "Aila: It's me! I don't think they saw me.";
                inHouseMessagesText[3] = "You: Aila!";
            } else if (messageCount == 7){
                inHouseMessagesText[0] = "*You run to the back door*";
                inHouseMessagesText[1] = "You: Aila, are you alright?";
                inHouseMessagesText[2] = "Aila: Yeah, just let me in so they don't spot me.";
                inHouseMessagesText[3] = "You: Okay.";
                special.setText("Open Door");
            } else if (messageCount == 8){
                inHouseMessagesText[0] = "*You open the door*";
                inHouseMessagesText[1] = "*Aila is there and no one else*";
                inHouseMessagesText[2] = "You: Aila..";
                inHouseMessagesText[3] = "*Aila has a gun in her hand*";

                eat.setEnabled(false);
                drink.setEnabled(false);

                special.setText("Next");
            } else if (messageCount == 9){
                inHouseMessagesText[0] = "*Aila holds up the gun at you*";
                inHouseMessagesText[1] = "*Her hand is on the trigger*";
                inHouseMessagesText[2] = "You: Aila?..";
                inHouseMessagesText[3] = "*She shoots the gun*";

                special.setText("Dodge");
            } else if (messageCount == 10){
                inHouseMessagesText[0] = "You: AILA! What are you doing?";
                inHouseMessagesText[1] = "*No response*";
                inHouseMessagesText[2] = "*She shoots the gun again*";

                special.setText("Dodge");
            } else if (messageCount == 11){
                inHouseMessagesText[0] = "You: AILA! Don't do this!!";
                inHouseMessagesText[1] = "*No response*";
                inHouseMessagesText[2] = "*She pulls out a knife and charges at you*";

                special.setText("Block");
            } else if (messageCount == 12){
                inHouseMessagesText[0] = "*You hold her arm away*";
                inHouseMessagesText[1] = "You: Aila! Stop this!!";
                inHouseMessagesText[2] = "*She pulls out her gun and aims it at you*";
                inHouseMessagesText[3] = "You: AILA!!";

                special.setText("Push away");
            } else if (messageCount == 13){
                inHouseMessagesText[0] = "You: You're not Aila, are you..";
                inHouseMessagesText[1] = "*No response*";
                inHouseMessagesText[2] = "You: I have no choice, do I...";
                inHouseMessagesText[3] = "You: I have to fight you.";

                special.setText("Equip Weapons");
            } else {
                equipSound.start();

                Intent toEventFour = new Intent(this, EventFour.class);

                final int result = 1;

                toEventFour.putExtra("health", health);
                toEventFour.putExtra("swordDamage", swordDamage);
                toEventFour.putExtra("bullets", bullets);
                toEventFour.putExtra("UsersName", usersName);

                startActivityForResult(toEventFour, result);
            }


            setTextViewText(inHouseMessages, inHouseMessagesText);
            printTextViews(inHouseMessages);

            messageCount++;
            scroll();
        }

        if (status.equals("Game End")) {
            blankLine();
            TextView[] approachCityMessages = new TextView[5];
            initializeTextViews(approachCityMessages);
            String[] approachCityMessagesText = new String[5];

            if (messageCount == 0) {
                approachCityMessagesText[0] = "*panting*";
                approachCityMessagesText[1] = "You: Aila...";
                approachCityMessagesText[2] = "You: You...";
            } else if (messageCount == 1) {
                approachCityMessagesText[0] = "You: You were my daughter..";
                approachCityMessagesText[1] = "You: Can what you said really be true..";
                approachCityMessagesText[2] = "You: How come I can't remember anything..";
            } else if (messageCount == 2) {
                approachCityMessagesText[0] = "You: I can remember Aila, Cyrus..";
                approachCityMessagesText[1] = "You: But I can't remember what exactly happened..";
                approachCityMessagesText[2] = "You: I can remember blurs, but that's it..";
            } else if (messageCount == 3) {
                approachCityMessagesText[0] = "You: What caused the world to be like this..";
                approachCityMessagesText[1] = "You: Did it really involve me..";
                approachCityMessagesText[2] = "You: Why don't I remember?!?!";
            } else if (messageCount == 4) {
                approachCityMessagesText[0] = "You: I can't even remember anyone else's face..";
                approachCityMessagesText[1] = "You: My only option..";
                approachCityMessagesText[2] = "You: is to look for answers..";
            } else if (messageCount == 5) {
                approachCityMessagesText[0] = "You: at that safe camp..";
                approachCityMessagesText[1] = "You: Cyrus told me about...";
            } else if (messageCount == 6) {
                approachCityMessagesText[0] = "You: over the mountain.";

                special.setText("Fin");
            } else {
                Intent toGameEnd = new Intent(this, GameEnd.class);

                toGameEnd.putExtra("health", health);
                toGameEnd.putExtra("hunger", hunger);
                toGameEnd.putExtra("thirst", thirst);
                toGameEnd.putExtra("food", food);
                toGameEnd.putExtra("water", water);
                toGameEnd.putExtra("swordDamage", swordDamage);
                toGameEnd.putExtra("bullets", bullets);
                toGameEnd.putExtra("UsersName", usersName);

                startActivity(toGameEnd);
                finish();
            }

            setTextViewText(approachCityMessages, approachCityMessagesText);
            printTextViews(approachCityMessages);


            messageCount++;
            scroll();
        }

        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        float vol = 0.1f; //This will be half of the default system sound
        am.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);
    }

    public void checkForEvents() {                                                        //Only path if's go in checkForEvents
        if (path >= 7 && path <= 9) {
            TextView[] eventOneBuildUpMessages = new TextView[1];
            String[] eventOneBuildUpMessagesText = new String[1];
            initializeTextViews(eventOneBuildUpMessages);

            if (path == 7) eventOneBuildUpMessagesText[0] = "*Is that.. a light?*";
            else if (path == 8) eventOneBuildUpMessagesText[0] = "*Maybe there's someone there*";
            else if (path == 9) eventOneBuildUpMessagesText[0] = "*Maybe I can find some help...*";

            setTextViewText(eventOneBuildUpMessages, eventOneBuildUpMessagesText);
            printTextViews(eventOneBuildUpMessages);
        }

        if (path == 10) {
            blankLine();

            TextView[] eventOneStartMessages = new TextView[3];
            String[] eventOneStartMessagesText = {"A person peeks out of a corner.",
                "He sees you and you see him.", "You two stare at each other."};
            initializeTextViews(eventOneStartMessages);
            setTextViewText(eventOneStartMessages, eventOneStartMessagesText);
            printTextViews(eventOneStartMessages);

            forward.setEnabled(false);
            eat.setEnabled(false);
            drink.setEnabled(false);
            special.setVisibility(View.VISIBLE);
            special.setText("Next");
            status = "Event One Start";
            messageCount = 0;
        }

        if (path == 20) {
            blankLine();

            TextView[] eventTwoStart = new TextView[4];
            String[] eventTwoStartMessages = {"*You hear a little girl screaming.*", "*You walk closer to the sound.*",
                    "*The screaming is coming from a cabin.*", "*You walk up to the door of the cabin.*"};
            initializeTextViews(eventTwoStart);
            setTextViewText(eventTwoStart, eventTwoStartMessages);
            printTextViews(eventTwoStart);

            forward.setEnabled(false);
            eat.setEnabled(false);
            drink.setEnabled(false);
            special.setVisibility(View.VISIBLE);
            special.setText("Go in");
            status = "Event Two Start";
            messageCount = 0;
        }

        if (path == 27) {
            gunDamageText.setVisibility(View.VISIBLE);
            bulletsText.setVisibility(View.VISIBLE);
            gunDamageTextDisplay.setVisibility(View.VISIBLE);
            bulletsTextDisplay.setVisibility(View.VISIBLE);

            TextView[] gunDiscovery = new TextView[4];
            String[] gunDiscoveryText = {"*You found a small pistol on the ground*", " ",
                "You: A gun...", "You: I guess I should keep this."};
            initializeTextViews(gunDiscovery);
            setTextViewText(gunDiscovery, gunDiscoveryText);

            blankLine();
            printTextViews(gunDiscovery);
        }

        if (path == 35) {
            forward.setEnabled(false);
            special.setVisibility(View.VISIBLE);

            special.setText("Find Shelter");
            status = "Rainstorm";
            messageCount = 0;

            TextView[] rainstorm = new TextView[1];
            initializeTextViews(rainstorm);
            rainstorm[0].setText("*A severe rainstorm comes out of nowhere*");

            blankLine();
            printTextViews(rainstorm);
        }

        if (path == 40) {
            forward.setEnabled(false);
            special.setVisibility(View.VISIBLE);
            special.setText("Next");
            status = "Event Three Start";
            messageCount = 0;

            TextView[] otherFamily = new TextView[4];
            String[] otherFamilyText = {"*You and Aila see a father and son*", "*They are pushing a cart and they see you*",
                    "Aila: " + usersName + "...", "Aila: What do we do?.."};
            initializeTextViews(otherFamily);
            setTextViewText(otherFamily, otherFamilyText);

            blankLine();
            printTextViews(otherFamily);
        }

        if (path == 45) {
            forward.setEnabled(false);
            special.setVisibility(View.VISIBLE);

            special.setText("Next");
            status = "Post-Event Three Talk";
            messageCount = 0;

            TextView[] rainstorm = new TextView[1];
            initializeTextViews(rainstorm);
            rainstorm[0].setText("Aila: Wait..");

            blankLine();
            printTextViews(rainstorm);
        }

        if (path >= 52 && path <= 54) {
            TextView[] eventOneBuildUpMessages = new TextView[1];
            initializeTextViews(eventOneBuildUpMessages);

            if (path == 52) eventOneBuildUpMessages[0].setText("*There are some buildings beyond the trees*");
            else if (path == 53) eventOneBuildUpMessages[0].setText("*Is that.. a city?*");
            else if (path == 54) eventOneBuildUpMessages[0].setText("*Are there people here?...*");

            printTextViews(eventOneBuildUpMessages);
        }
        
        if (path == 55) {
            blankLine();

            TextView[] approachCity = new TextView[2];
            String[] approachCityMessages = { "*Suddenly, a sniper shot comes out of nowhere*",
                    "*It barely misses you*"};
            initializeTextViews(approachCity);
            setTextViewText(approachCity, approachCityMessages);
            printTextViews(approachCity);
            
            forward.setEnabled(false);
            eat.setEnabled(false);
            drink.setEnabled(false);
            special.setVisibility(View.VISIBLE);
            special.setText("Hide");
            status = "Approach City";
            messageCount = 0;
        }

        if (path == 60) {
            TextView[] inHouse = new TextView[3];
            String[] inHouseMessages = {"*You find an old house outside the city*", 
                    "*You go inside it and hide*", "*The sniper shots stop*"};
            initializeTextViews(inHouse);
            setTextViewText(inHouse, inHouseMessages);
            
            blankLine();
            printTextViews(inHouse);
            
            forward.setEnabled(false);

            forward.setText("Forward");
            eat.setText("Eat");
            drink.setText("Drink");

            special.setVisibility(View.VISIBLE);
            special.setText("Next");
            status = "In House";
            messageCount = 0;
        }

        if (path == 69) {
            TextView[] inHouse = new TextView[3];
            String[] inHouseMessages = {"*You escaped the city*",
                    "*There don't hear any more sniper shots*", "*You are safe*"};
            initializeTextViews(inHouse);
            setTextViewText(inHouse, inHouseMessages);

            blankLine();
            printTextViews(inHouse);

            forward.setEnabled(false);
            eat.setEnabled(false);
            drink.setEnabled(false);

            special.setVisibility(View.VISIBLE);
            special.setText("Next");
            status = "Game End";
            messageCount = 0;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        status = data.getStringExtra("status");

        if (status.equals("Game Over")) {
            Intent gameOver = new Intent(this, GameOver.class);
            startActivity(gameOver);
            finish();
        }

        if (status.equals("Event One Complete")) {
            backHealth = data.getStringExtra("newHealth2");
            health = Integer.parseInt(backHealth);

            healthText.setText("" + health);

            special.setText("Cook Cyrus");
            forward.setEnabled(true);
            eat.setEnabled(true);
            drink.setEnabled(true);

            TextView[] postEventOne = new TextView[8];
            String[] postEventOneText = {"_______________________", " ",
                    "I can remember Cyrus' face, but I can't remember anything else..",
                    "I don't even remember my family, just blurs..",
                    "I guess I should head towards the mountain.",
                    "I'll be safe there..", " ", "*Special Option: Cook Cyrus --- Continue Forward to Skip*"};
            initializeTextViews(postEventOne);
            setTextViewText(postEventOne, postEventOneText);
            postEventOne[0].setGravity(Gravity.CENTER);
            printTextViews(postEventOne);

            scroll();
        }
        
        if (status.equals("Event Two Complete")) {
            backHealth = data.getStringExtra("newHealth2");
            health = Integer.parseInt(backHealth);

            healthText.setText("" + health);

            forward.setEnabled(true);
            eat.setEnabled(true);
            drink.setEnabled(true);
            special.setVisibility(View.INVISIBLE);

            TextView[] postEventTwo = new TextView[2];
            String[] postEventTwoText = {"_______________________",
                    "*You and Aila are now traveling together*"};
            initializeTextViews(postEventTwo);
            setTextViewText(postEventTwo, postEventTwoText);
            postEventTwo[0].setGravity(Gravity.CENTER);
            printTextViews(postEventTwo);
            
            scroll();
        }

        if (status.equals("Event Three Complete")) {
            backHealth = data.getStringExtra("newHealth2");
            backBullets = data.getStringExtra("bullets");
            health = Integer.parseInt(backHealth);
            bullets = Integer.parseInt(backBullets);

            healthText.setText("" + health);
            bulletsText.setText("" + bullets);

            TextView[] postEventThree = new TextView[2];
            String[] postEventThreeText = {"_______________________",
                    "*You and Aila walk up to the boy*"};
            initializeTextViews(postEventThree);
            setTextViewText(postEventThree, postEventThreeText);
            postEventThree[0].setGravity(Gravity.CENTER);
            printTextViews(postEventThree);

            special.setText("Next");
            messageCount = 0;
            
            scroll();
        }

        if (status.equals("Event Four Complete")) {
            backHealth = data.getStringExtra("newHealth2");
            backBullets = data.getStringExtra("bullets");
            health = Integer.parseInt(backHealth);
            bullets = Integer.parseInt(backBullets);

            healthText.setText("" + health);
            bulletsText.setText("" + bullets);

            TextView[] postEventThree = new TextView[4];
            String[] postEventThreeText = {"_______________________",
                    "You: I'm.. so confused.", "You: What is happening?..",
                    "You: I need to get out of here."};
            initializeTextViews(postEventThree);
            setTextViewText(postEventThree, postEventThreeText);
            postEventThree[0].setGravity(Gravity.CENTER);
            printTextViews(postEventThree);

            forward.setEnabled(true);
            eat.setEnabled(true);
            drink.setEnabled(true);
            special.setVisibility(View.INVISIBLE);

            scroll();
        }
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

        super.onStop();
    }

    public void onLeft() {
        path++;

        TextView[] messages = new TextView[2];
        initializeTextViews(messages);

        if (path == 56 || path == 58) {
            health -= 10;
            healthText.setText("" + health);

            messages[0].setText("-10 health: hit by sniper bullet");
            messages[1].setText("*Ah! I can't get shot again...*");
        } else {
            messages[0].setText("-0 health: dodged sniper bullet");
            messages[1].setText("*Let's find some cover*");
        }

        runningSound.start();

        blankLine();
        printTextViews(messages);
        scroll();

        checkForEvents();
    }

    public void onForward() {
        path++;

        TextView[] messages = new TextView[2];
        initializeTextViews(messages);

        if (path == 57) {
            health -= 10;
            healthText.setText("" + health);

            messages[0].setText("-10 health: hit by sniper bullet");
            messages[1].setText("*Ugh, this is gonna slow me down..*");
        } else {
            messages[0].setText("-0 health: dodged sniper bullet");
            messages[1].setText("*I got to keep this up*");
        }

        runningSound.start();

        blankLine();
        printTextViews(messages);
        scroll();

        checkForEvents();
    }

    public void onRight() {
        path++;

        TextView[] messages = new TextView[2];
        initializeTextViews(messages);

        if (path == 59 || path == 60) {
            health -= 10;
            healthText.setText("" + health);

            messages[0].setText("-10 health: hit by sniper bullet");
            messages[1].setText("*Not good..*");
        } else {
            messages[0].setText("-0 health: dodged sniper bullet");
            messages[1].setText("*I need to find Aila*");
        }

        runningSound.start();

        blankLine();
        printTextViews(messages);
        scroll();

        checkForEvents();
    }

    public void initializeTextViews(TextView[] textViews) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i] = new TextView(this);
            textViews[i].setTextColor(Color.BLACK);
            textViews[i].setTypeface(Typeface.create("monospace", Typeface.NORMAL));
        }
    }

    public void setTextViewText(TextView[] textViews, String[] text) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setText(text[i]);
        }
    }

    public void printTextViews(TextView[] textViews) {
        for (int i = 0; i < textViews.length; i++) {
            if (textViews[i].getText().toString().length() != 0 ) ((LinearLayout) linear).addView(textViews[i]);
        }
    }

    public void addToConsole(String[] array) {
        for (int i = 0; i < array.length; i++) {
            consoleMessages.add(array[i]);
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