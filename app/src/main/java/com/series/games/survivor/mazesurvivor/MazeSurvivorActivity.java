package com.series.games.survivor.mazesurvivor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.series.games.survivor.SurvivorGameMenu;
import com.series.survivor.survivorgames.R;

import java.util.ArrayList;


public class MazeSurvivorActivity extends Activity {

    private MazeSurvivorView myGLView;
    private float screenWidth;
    private float screenHeight;
    private float ratio;
    //borders of direction control buttons' touch areas
    private float buttonLeftBorder;
    private float buttonRightBorder;
    private float buttonUpBorder;
    private float buttonMidBorder;
    private float attackButtonLeftBorder;
    private float bonusTimeLeftBorder;
    private float bonusTimeBottomBorder;
    //Variables used for recreating sound effects for game
    private SoundPool sounds;
    private int sndwalk;
    private int sndattack;
    private int sndbonustime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Receive the message from SurvivorGamesMenu Activity
        Intent intent = getIntent();
        String message = intent.getStringExtra(SurvivorGameMenu.EXTRA_MESSAGE);
        ArrayList<Character> levelArray = new ArrayList<Character>();
        int charIndex = 0;
        while(message.charAt(charIndex) != ' ') {
            levelArray.add(message.charAt(charIndex));
            charIndex++;
        }
        charIndex++;
        char mode = message.charAt(charIndex);
        //convert string to int
        int level = 0;
        //get the initial level from menu activity
        for (int index = 0; index < levelArray.size(); index++) {
            level += ((levelArray.get(index) - '0') * Math.pow(10, levelArray.size() - 1 - index));
        }

        //get the screen's width and height ratio
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        ratio = screenWidth / screenHeight;

        buttonLeftBorder = screenWidth / 3;
        buttonRightBorder = screenWidth * 2 / 3;
        buttonUpBorder = (screenHeight + screenWidth) / 2;
        buttonMidBorder = (screenHeight + screenWidth) / 2 + (screenHeight - screenWidth) / 4;
        attackButtonLeftBorder = screenWidth * 3 / 4;
        bonusTimeLeftBorder = screenWidth / 2;
        bonusTimeBottomBorder = screenWidth + (screenHeight - screenWidth) / 4;

        //used for sounds effect game
        sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        sndwalk = sounds.load(this, R.raw.walk, 1);
        sndattack = sounds.load(this, R.raw.fireattack, 1);
        sndbonustime = sounds.load(this, R.raw.bonustime, 1);

        //Create an instance of GLSurfaceView
        //and set it as the content view
        myGLView = new MazeSurvivorView(this, level, ratio, mode);
        setContentView(myGLView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://According to the touch down position
                //move survivor to the corresponding direction, by 1 step
                //Or attack the monster
                if(x < buttonLeftBorder && y > buttonUpBorder) {
                    if(myGLView.myRenderer.mazeWorld.survivor.isAlive) {
                        sounds.play(sndwalk, 1, 1, 1, 0, 1);
                    }
                    myGLView.myRenderer.updateSurvivor("LEFT");
                } else if(x > buttonRightBorder && y > buttonUpBorder) {
                    if(myGLView.myRenderer.mazeWorld.survivor.isAlive) {
                        sounds.play(sndwalk, 1, 1, 1, 0, 1);
                    }
                    myGLView.myRenderer.updateSurvivor("RIGHT");
                } else if(y < buttonMidBorder && y > buttonUpBorder && x > buttonLeftBorder && x < buttonRightBorder) {
                    if(myGLView.myRenderer.mazeWorld.survivor.isAlive) {
                        sounds.play(sndwalk, 1, 1, 1, 0, 1);
                    }
                    myGLView.myRenderer.updateSurvivor("UP");
                } else if(y > buttonMidBorder && x > buttonLeftBorder && x < buttonRightBorder) {
                    if(myGLView.myRenderer.mazeWorld.survivor.isAlive) {
                        sounds.play(sndwalk, 1, 1, 1, 0, 1);
                    }
                    myGLView.myRenderer.updateSurvivor("DOWN");
                } else if(x > attackButtonLeftBorder && y > screenWidth && y < buttonUpBorder) {//Attack Button touched
                    if(myGLView.myRenderer.mazeWorld.survivor.isAlive) {
                        sounds.play(sndattack, 1, 1, 1, 0, 1);
                    }
                    myGLView.myRenderer.updateFireAttack();
                } else if(x > bonusTimeLeftBorder && x < attackButtonLeftBorder && y > screenWidth && y < bonusTimeBottomBorder) {//BonusTime Button touched
                    if(myGLView.myRenderer.mazeWorld.survivor.isAlive) {
                        sounds.play(sndbonustime, 1, 1, 1, 0, 1);
                    }
                    myGLView.myRenderer.updateChangeTime();
                } else if(y < screenWidth) {//Game is paused
                    myGLView.myRenderer.updatePausedStatus();
                }
            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_UP:

        }
        return false;
    }
}
