package com.example.notesquirrel;


import static androidx.appcompat.app.AlertDialog.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class ImageActivity extends AppCompatActivity implements PointCollecterListener {

    private static final String PASSWORD_SET = "PASSWORD_SET";
    private static final int POINT_CLOSENESS = 40;
    private PointCollector pointCollector = new PointCollector();
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        
        addTouchListener();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            Boolean resetPasspoints = extras.getBoolean(MainActivity.RESET_PASSPOINTS);
            if(resetPasspoints) {
                SharedPreferences pref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(PASSWORD_SET, false);
                editor.commit();
            }
        }

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Boolean passpointsSet = prefs.getBoolean(PASSWORD_SET, false);
        if(!passpointsSet) {
            showSetPasspointsPrompt();
        }

        pointCollector.setListener(this);
    }

    private void showSetPasspointsPrompt() {
        AlertDialog.Builder builder = new Builder(this);

        builder.setPositiveButton("ok", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setTitle("Create your Passport Sequence");
        builder.setMessage("Touch four point on the image to set the passpoint sequence." +
                " You must click the same points in the future to gain access to your notes.");

        AlertDialog dlg = builder.create();

        dlg.show();
    }

    private void addTouchListener() {
        ImageView image = findViewById(R.id.touch_image);

        image.setOnTouchListener(pointCollector);
    }

    private void savePasspoints(final List<Point> points) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.storing_data);

        final AlertDialog dlg = builder.create();
        dlg.show();

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                db.storePoints(points);
                Log.d(MainActivity.DEBUGTAG, "Points saved " + points.size());

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                SharedPreferences pref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(PASSWORD_SET, true);
                editor.commit();

                pointCollector.clear();
                dlg.dismiss();
            }
        };

        task.execute();
    }

    private void verifyPasspoints(final List<Point> touchedPoints) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Checking passpoints...");
        final AlertDialog dlg = builder.create();
        dlg.show();

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                List<Point> savedPoints = db.getPoints();

                Log.d(MainActivity.DEBUGTAG, "Loaded points: " + savedPoints.size());

                if(savedPoints.size() != PointCollector.NUM_POINTS
                        || touchedPoints.size() != PointCollector.NUM_POINTS) {
                    return false;
                }

                for (int i=0; i< PointCollector.NUM_POINTS; i++) {
                    Point savePoint = savedPoints.get(i);
                    Point touchedPoint = touchedPoints.get(i);

                    int xDiff = savePoint.x - touchedPoint.x;
                    int yDiff = savePoint.y - touchedPoint.y;

                    int distSquared = xDiff*xDiff + yDiff+yDiff;

                    Log.d(MainActivity.DEBUGTAG, "Distance squared: " + distSquared);

                    if(distSquared > POINT_CLOSENESS*POINT_CLOSENESS) {
                        return false;
                    }
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean pass) {

                dlg.dismiss();
                pointCollector.clear();

                if(pass) {
                    Intent i = new Intent(ImageActivity.this, MainActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(ImageActivity.this, "Access Denied", Toast.LENGTH_LONG).show();
                }
            }
        };

        task.execute();
    }

    @Override
    public void pointsCollected(List<Point> points) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Boolean passpointsSet = prefs.getBoolean(PASSWORD_SET, false);

        if(!passpointsSet) {
            Log.d(MainActivity.DEBUGTAG, "Saving passpoints...");
            savePasspoints(points);
        }else {
            Log.d(MainActivity.DEBUGTAG, "Verifying passpoints...");
            verifyPasspoints(points);
        }
    }

    /*
    @Override
    public void pointsCollected(List<Point> points) {
        Log.d(MainActivity.DEBUGTAG, "Collected Points " + points.size());

        db.storePoints(points);

        List<Point> list = db.getPoints();

        for (Point point: points) {
            Log.d(MainActivity.DEBUGTAG, String.format("Got Point: (%d, %d)", point.x, point.y));
        }
    }
    */
}