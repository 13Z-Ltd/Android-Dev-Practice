package com.example.notesquirrel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    public static final String DEBUGTAG = "13Z";
    public static final String TEXTFILE = "notesquirrel.txt";
    public static final String FILESAVED = "FileSaved";
    public static final String RESET_PASSPOINTS = "ResetPasspoints";
    public static final int BROWSE_GALLERY_REQUEST = 1;
    private Uri image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addSaveButtonListener();
        addLockButtonLListener();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean fileSaved = prefs.getBoolean(FILESAVED, false);
        if (fileSaved) {
            loadSaveFile();
        }
    }

    private void loadSaveFile() {
        try {
            FileInputStream fis = openFileInput(TEXTFILE);

            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fis)));

            EditText editor = (EditText) findViewById(R.id.textEditor);

            String line;
            while ((line = reader.readLine()) != null){
                editor.append(line + "\n");
            }

            fis.close();
        } catch (Exception e) {
            Log.d(DEBUGTAG, "Unable to read file: ");
        }
    }

    private void replaceImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.replace_image, null);
        builder.setTitle(R.string.replace_lock_image);
        builder.setView(v);

        final AlertDialog dlg = builder.create();
        dlg.show();

        Button takePhoto = (Button) dlg.findViewById(R.id.take_photo);
        Button browseGallery = (Button) dlg.findViewById(R.id.browse_gallery);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        browseGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browseGallery();
            }
        });
    }

    private void browseGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, BROWSE_GALLERY_REQUEST);
    }

    private void takePhoto() {
    }

    private void addSaveButtonListener() {
        Button saveBtn = (Button) findViewById(R.id.save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText();
                //editor.setText("");
            }
        });
    }

    private void saveText() {
        EditText editText = (EditText) findViewById(R.id.textEditor);
        String text = editText.getText().toString();

        try {
            FileOutputStream fos = openFileOutput(TEXTFILE, Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();

            SharedPreferences pref = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(FILESAVED, true);
            editor.commit();
        } catch (Exception e) {
            Log.d(DEBUGTAG, "Unable to save file: ");
            Toast.makeText(MainActivity.this, R.string.toast_cant_save, Toast.LENGTH_LONG).show();
        }
    }

    private void addLockButtonLListener() {
        Button lockButton = findViewById(R.id.lock);

        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ImageActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_passpoint_reset:
                Intent i = new Intent(MainActivity.this, ImageActivity.class);
                i.putExtra(RESET_PASSPOINTS, true);
                startActivity(i);
                return true;
            case R.id.menu_replace_image:
                replaceImage();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == BROWSE_GALLERY_REQUEST) {
            //Toast.makeText(this, "Gallery result" + data.getData(), Toast.LENGTH_LONG).show();

            String[] columns = {MediaStore.Images.Media.DATA};

            Uri imageUri = data.getData();
            Cursor cursor = getContentResolver().query(imageUri, columns, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(columns[0]);
            String imagePath = cursor.getString(columnIndex);

            cursor.close();

            image = Uri.parse(imagePath);
            
            if(image == null) {
                Toast.makeText(this, "Unable To display image", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Log.d(DEBUGTAG, "Photo: " + image.getPath());

            Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}