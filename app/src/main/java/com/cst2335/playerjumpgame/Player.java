package com.cst2335.playerjumpgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player {
    private float x, y;
    private int radius = 30;
    private Paint paint;
    private float velocityY;
    private final float gravity = 1.5f;
    private final float jumpStrength = -20f;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        this.paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    public void update() {
        velocityY += gravity;
        y += velocityY;
    }

    public void jump() {
        velocityY = jumpStrength;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }
}