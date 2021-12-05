package com.BZ.tennisdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Bitmap button;
    private GameRunner runner;
    private Game game;

    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        SurfaceHolder holder = getHolder();

        holder.addCallback(this);
        //button = BitmapFactory.decodeResource(getResources(), R.drawable.button);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        game.onTouchEvent(event);
        return true;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Log.d("13Z", "created");
        game = new Game(getContext(), getWidth(), getHeight(), surfaceHolder, getResources());
        runner = new GameRunner(game);
        runner.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        Log.d("13Z", "destroyed");
        if(runner != null) {
            runner.shutdown();

            while (runner != null) {
                try {
                    runner.join();
                    runner = null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {

    }


    /*

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        SurfaceHolder holder = getHolder();

        Canvas canvas = holder.lockCanvas();

        if(canvas != null) {
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(button, 50, 50, null);

            holder.unlockCanvasAndPost(canvas);
        }

        return true;
    }
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.d("13Z", "changed");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("13Z", "created");
        runner = new GameRunner();
        runner.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("13Z", "destroyed");
        if(runner != null) {
            runner.shutdown();

            while (runner != null) {
                try {
                    runner.join();
                    runner = null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
     */
}
