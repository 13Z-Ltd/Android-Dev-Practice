package com.example.notesquirrel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final String POINTS_TABLE = "POINTS";
    private static final String COL_ID = "ID";
    private static final String COL_X = "X";
    private static final String COL_Y = "Y";


    public Database(@Nullable Context context) {
        super(context, "notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s INTEGER PRIMARY KEY, %s INTEGER NOT NULL, %s INTEGER NOT NULL)",
                POINTS_TABLE, COL_ID, COL_X, COL_Y);

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void storePoints(List<Point> points) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(POINTS_TABLE, null, null);

        int i = 0;
        for (Point point: points){
            ContentValues values = new ContentValues();

            values.put(COL_ID, i);
            values.put(COL_X, point.x);
            values.put(COL_Y, point.y);

            db.insert(POINTS_TABLE, null, values);

            i++;
        }

        db.close();
    }

    public List<Point> getPoints() {
        List<Point> points = new ArrayList<Point>();

        SQLiteDatabase db = getReadableDatabase();

        String sql = String.format("SELECT %s, %s FROM %s ORDER BY %s", COL_X, COL_Y, POINTS_TABLE, COL_ID);
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int x = cursor.getInt(0);
            int y = cursor.getInt(1);

            points.add(new Point(x, y));
        }

        db.close();
        return points;
    }
}
