package com.BZ.tennisdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class Game {

    private Context context;

    private enum State {
        PAUSED, WON, LOST, RUNNING
    }
    private State state = State.PAUSED;

    private final SoundPool soundPool;

    private SurfaceHolder holder;
    private Resources resources;

    private Ball ball;
    private Bat player;
    private Bat opponent;

    private Paint textPaint;
    private int[] sounds = new int[5];

    public Game(Context context, int width, int height, SurfaceHolder holder, Resources resources) {
        this.context = context;
        this.holder = holder;
        this.resources = resources;

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        ball = new Ball(width, height);
        player = new Bat(width, height, Bat.Position.LEFT);
        opponent = new Bat(width, height, Bat.Position.RIGHT);

        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(60);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void init() {
        Bitmap ballImage = BitmapFactory.decodeResource(resources, R.drawable.button);
        Bitmap ballShadow = BitmapFactory.decodeResource(resources, R.drawable.buttonshadow);
        Bitmap batImage = BitmapFactory.decodeResource(resources, R.drawable.bat);
        Bitmap batShadow = BitmapFactory.decodeResource(resources, R.drawable.batshadow);

        ball.init(ballImage, ballShadow);
        player.init(batImage, batShadow);
        opponent.init(batImage, batShadow);

        sounds[Sounds.START] = soundPool.load(context, R.raw.start, 1);
        sounds[Sounds.WIN] = soundPool.load(context, R.raw.win, 1);
        sounds[Sounds.LOSE] = soundPool.load(context, R.raw.lose, 1);
        sounds[Sounds.BOUNCE1] = soundPool.load(context, R.raw.bounce1, 1);
        sounds[Sounds.BOUNCE2] = soundPool.load(context, R.raw.bounce2, 1);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                if(sounds[Sounds.START] == i) {
                    soundPool.play(i, 1, 1, 1, 0, 1);
                }
            }
        });
    }

    public void update(long elapsed) {
        if(state == State.RUNNING) {
            updateGame(elapsed);
        }
    }
    private void initObjectPositions(){
        ball.initPosition();
        player.initPosition();
        opponent.initPosition();
    }

    private void updateGame(long elapsed) {
        if(player.getScreenRect().contains(ball.getScreenRect().left, ball.getScreenRect().centerY())) {
            ball.moveRight();
            soundPool.play(sounds[Sounds.BOUNCE1], 1, 1, 1, 0, 1);
        }
        else if(opponent.getScreenRect().contains(ball.getScreenRect().right, ball.getScreenRect().centerY())) {
            ball.moveLeft();
            soundPool.play(sounds[Sounds.BOUNCE2], 1, 1, 1, 0, 1);
        }
        else if(ball.getScreenRect().left < player.getScreenRect().left) {
            Log.d("13Z", "lost");
            state = State.LOST;
            soundPool.play(sounds[Sounds.LOSE], 1, 1, 1, 0, 1);
            initObjectPositions();
        }
        else if(ball.getScreenRect().right > opponent.getScreenRect().right) {
            Log.d("13Z", "Won");
            state = State.WON;
            soundPool.play(sounds[Sounds.WIN], 1, 1, 1, 0, 1);
            initObjectPositions();
        }

        ball.update(elapsed);
        opponent.update(elapsed, ball);
    }

    private void drawText(Canvas canvas, String text) {

        canvas.drawText(text, canvas.getWidth() / 2, canvas.getHeight()/2, textPaint);
    }

    public void draw(){
        Canvas canvas = holder.lockCanvas();

        if(canvas != null) {
            canvas.drawColor(Color.WHITE);

            switch (state){
                case PAUSED:
                    drawText(canvas, "Tap screen to start...");
                    break;
                case WON:
                    drawText(canvas, "You WON!!!");
                    break;
                case LOST:
                    drawText(canvas, "You lost :(((");
                    break;
                case RUNNING:
                    drawGame(canvas);
                    break;
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawGame(Canvas canvas) {
            //canvas.drawColor(Color.WHITE);
            ball.draw(canvas);
            player.draw(canvas);
            opponent.draw(canvas);
    }

    public void onTouchEvent(MotionEvent event) {
        if(state == State.RUNNING) {
            player.setPosition(event.getY());
        }else {
            state = State.RUNNING;
        }
    }
}
