package com.BZ.tennisdemo;

import android.util.Log;

public class GameRunner extends Thread {

    private Game game;
    private volatile boolean running = true;

    public GameRunner(Game game) {
        this.game = game;
    }

    @Override
    public void run() {

        game.init();
        long lastTime = System.currentTimeMillis();

        //Game loop
        while(running) {
            // Draw Stuff.
            /*Log.d("13Z", "Thread running");

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             */
            long now = System.currentTimeMillis();
            long elapsed = now - lastTime;

            if(elapsed < 100) {
                game.update(elapsed);
                game.draw();
            }else {
                Log.d("13Z", "Elapsed time is greater then 100");
            }

            lastTime = now;
        }
    }

    public void shutdown() {
        running = false;
    }
}
