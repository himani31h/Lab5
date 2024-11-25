package com.cst2335.playerjumpgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {
    private Thread gameThread;
    private boolean isPlaying;
    private SurfaceHolder holder;
    private Paint paint;
    private Player player;
    private ArrayList<Platform> platforms;
    private int score;
    private boolean isGameOver;
    private boolean isGameStarted;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        paint = new Paint();
        player = new Player(300, 500);
        platforms = new ArrayList<>();
        score = 0;
        isGameOver = false;  // Initially set to false, game is not over
        isGameStarted = false;  // Game hasn't started yet

        // Add the SurfaceHolder callback to initialize after layout
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initializePlatforms(); // Create platforms once the surface is created
                resume(); // Start the game
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                pause();
            }
        });
    }

    // Initialize platforms after layout is created
    private void initializePlatforms() {
        int platformCount = 10; // Adjust this number to control the number of platforms
        platforms.clear();

        // Generate platforms with dynamic positioning (spreading left to right)
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        for (int i = 0; i < platformCount; i++) {
            // Platforms will move horizontally with screen width spread
            float x = (i * 300) % screenWidth;  // wrap within screen width
            float y = 300 + i * 100;  // Vertically spaced platforms
            platforms.add(new Platform(x, y, 200, 20));
        }
    }

    @Override
    public void run() {
        while (isPlaying) {
            if (!isGameOver && isGameStarted) {
                update();
                draw();
            } else {
                if (!isGameStarted) {
                    drawStartScreen();  // Show "Tap to Start" before the game starts
                } else {
                    drawGameOverScreen();  // Show the "Game Over" screen after the game ends
                }
            }
            sleep();
        }
    }

    // Update game logic, move platforms left to right, and check for collisions
    public void update() {
        player.update();
        for (Platform platform : platforms) {
            platform.update(getWidth());  // Pass the screen width to the platform for wrapping

            // If player lands on a platform, jump and score
            if (platform.collidesWith(player)) {
                player.jump();
                score++;
            }
        }

        // Check if player falls below the screen
        if (player.getY() > getHeight()) {
            isGameOver = true;  // Only set to true if the player falls off the screen
        }
    }

    // Draw game elements including player, platforms, and score
    public void draw() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.rgb(100, 50, 150)); // Background color

            // Draw player and platforms
            player.draw(canvas);
            for (Platform platform : platforms) {
                platform.draw(canvas);
            }

            // Draw score on screen
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            canvas.drawText("Score: " + score, 50, 100, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }

    // Show the start screen with the "Tap to Start" message before game starts
    public void drawStartScreen() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.WHITE);
            paint.setTextSize(100);
            canvas.drawText("Tap to Start", getWidth() / 4, getHeight() / 2 - 100, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }

    // Game over screen display
    public void drawGameOverScreen() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.WHITE);
            paint.setTextSize(100);
            canvas.drawText("Game Over", getWidth() / 4, getHeight() / 2 - 100, paint);
            paint.setTextSize(50);
            canvas.drawText("Tap to Start", getWidth() / 3, getHeight() / 2 + 50, paint);
            canvas.drawText("Score: " + score, getWidth() / 3, getHeight() / 2 + 150, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }

    // Control the frame rate by sleeping the thread
    private void sleep() {
        try {
            Thread.sleep(17); // ~60 FPS
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Handle touch events to make the player jump or restart game
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver && event.getAction() == MotionEvent.ACTION_DOWN) {
            restartGame();  // Restart the game when tapped
        } else if (!isGameOver && event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isGameStarted) {
                isGameStarted = true;  // Start the game when the player taps
            }
            player.jump();  // Make the player jump when tapped
        }
        return true;
    }

    // Restart the game by resetting player and platform positions
    private void restartGame() {
        isGameOver = false;
        isGameStarted = false;  // Reset to false to trigger the game start logic
        player = new Player(300, 500);
        initializePlatforms();  // Reinitialize platforms for a fresh game
        score = 0;
    }

    // Start/resume the game
    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Pause the game
    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}