package com.BZ.tennisdemo;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

public class Ball extends Sprite{

    private final float speedX = 0.4f;
    private final float speedY = 0.4f;

    private int directionX;
    private int directionY;

    public Ball(int screenWidth, int screenHeight) {
        super(screenWidth, screenHeight);
    }

    @Override
    public void init(Bitmap image, Bitmap shadow) {
        super.init(image, shadow);
        initPosition();

        Random random = new Random();

        directionX = random.nextInt(2)*2 - 1;
        directionY = random.nextInt(2)*2 - 1;
    }

    public void initPosition() {
        setX(getScreenWidth() / 2 - getRect().centerX());
        setY(getScreenHeight() / 2 - getRect().centerY());
    }

    public void update(long elapsed) {
        float x = getX();
        float y = getY();

        Rect screenRect = getScreenRect();
        if(screenRect.left <= 0) {
            directionX = 1;
        }
        else if(screenRect.right >= getScreenWidth()) {
            directionX = -1;
        }

        if(screenRect.top <= 0) {
            directionY = 1;
        }
        else if(screenRect.bottom >= getScreenHeight()) {
            directionY = -1;
        }

        x += directionX * speedX * elapsed;
        y += directionY * speedY * elapsed;

        setX(x);
        setY(y);
    }

    public void moveRight() {
        directionX = 1;
    }

    public void moveLeft() {
        directionX = -1;
    }
}
