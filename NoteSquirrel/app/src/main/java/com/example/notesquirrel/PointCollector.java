package com.example.notesquirrel;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PointCollector implements View.OnTouchListener {

    public static final int NUM_POINTS = 4;
    private PointCollecterListener listener;
    private List<Point> points = new ArrayList<Point>();

    public boolean onTouch(View view, MotionEvent motionEvent) {

        int x = Math.round(motionEvent.getX());
        int y = Math.round(motionEvent.getY());

        String message = String.format("Coordinates: (%d, %d)", x, y);

        Log.d(MainActivity.DEBUGTAG, message);

        points.add(new Point(x, y));

        if(points.size() == NUM_POINTS) {
            if(listener != null){
                listener.pointsCollected(points);
            }
        }

        return false;
    }

    public void setListener(PointCollecterListener listener) {
        this.listener = listener;
    }

    public void clear(){
        points.clear();
    }
}
