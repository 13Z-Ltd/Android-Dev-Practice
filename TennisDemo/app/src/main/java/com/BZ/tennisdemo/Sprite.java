package com.BZ.tennisdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
    public float x;
    public float y;

    private int screenWidth;
    private int screenHeight;
    private Bitmap image;
    private Bitmap shadow;

    private Rect bounds;

    public Sprite(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void init(Bitmap image, Bitmap shadow) {
        this.image = image;
        this.shadow = shadow;

        bounds = new Rect(0,0,image.getWidth(), image.getHeight());
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(shadow, x, y, null);
        canvas.drawBitmap(image, x + 3, y, null);
    }

    public Rect getRect() {
        return bounds;
    }

    public Rect getScreenRect() {
        return new Rect((int)x, (int)y, (int)x +getRect().width(), (int)y + getRect().height());
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }
}
