package com.cst2335.playerjumpgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoreDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "scores.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "scores";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SCORE = "score";

    public ScoreDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SCORE + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void saveScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, score);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int getHighScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + COLUMN_SCORE + ") FROM " + TABLE_NAME, null);
        int highScore = 0;
        if (cursor.moveToFirst()) {
            highScore = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return highScore;
    }
}