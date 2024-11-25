package com.cst2335.playerjumpgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Platform {
    private float x, y;
    private float width, height;
    private Paint paintBody, paintEyes, paintBeak;

    public Platform(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // Initialize paints
        paintBody = new Paint();
        paintBody.setColor(0xFF228B22); // ForestGreen

        paintEyes = new Paint();
        paintEyes.setColor(0xFFFFFFFF); // White for eyes

        paintBeak = new Paint();
        paintBeak.setColor(0xFFFFA500); // Orange for beak
    }

    public void update(int screenWidth) {
        x += 5; // Move platforms to the right
        if (x > screenWidth) { // Reset platform to the left
            x = -width;
        }
    }

    public void draw(Canvas canvas) {
        // Draw body (green rounded rectangle)
        RectF body = new RectF(x, y, x + width, y + height);
        canvas.drawRoundRect(body, 30, 30, paintBody); // Rounded rectangle for the body

        // Draw eyes (white circles)
        float eyeRadius = height / 4;
        float eyeY = y + height / 3;
        float eyeLeftX = x + width / 3;
        float eyeRightX = x + (2 * width / 3);
        canvas.drawCircle(eyeLeftX, eyeY, eyeRadius, paintEyes); // Left eye
        canvas.drawCircle(eyeRightX, eyeY, eyeRadius, paintEyes); // Right eye

        // Draw beak (orange triangle)
        float beakX1 = x + width / 2; // Tip of the beak
        float beakY1 = y + (2 * height / 3);
        float beakX2 = eyeLeftX + eyeRadius / 2; // Left base of the beak
        float beakY2 = y + height;
        float beakX3 = eyeRightX - eyeRadius / 2; // Right base of the beak
        canvas.drawPath(createTriangle(beakX1, beakY1, beakX2, beakY2, beakX3, beakY2), paintBeak);
    }

    private android.graphics.Path createTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        android.graphics.Path path = new android.graphics.Path();
        path.moveTo(x1, y1); // Tip of the triangle
        path.lineTo(x2, y2); // Bottom-left corner
        path.lineTo(x3, y3); // Bottom-right corner
        path.close();
        return path;
    }

    public boolean collidesWith(Player player) {
        return player.getY() + player.getRadius() > y &&
                player.getY() - player.getRadius() < y + height &&
                player.getX() > x &&
                player.getX() < x + width;
    }
}
