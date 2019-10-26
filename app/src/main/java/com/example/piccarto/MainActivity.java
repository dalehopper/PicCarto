package com.example.piccarto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.room.Room;

public class MainActivity extends AppCompatActivity {
    OverlayDao overlayDao;
    Bitmap bitmap;
    String email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "overlay-database")
                .allowMainThreadQueries()
                .build();
        overlayDao = database.overlayDao();

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        setContentView(R.layout.activity_main);
        final TableLayout table = findViewById(R.id.main_table);

        for(Overlay overlay : overlayDao.loadUserOverlays(email)){


                LinearLayout.LayoutParams tableRowParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                final TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(tableRowParams);
                tableRow.setGravity(Gravity.START);
                ImageButton imageButton = new ImageButton(this);
                final Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                deleteButton.setId(overlay.getOverlayID());
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        overlayDao.delete(overlayDao.findById(v.getId()));
                        table.removeView(tableRow);

                    }
                });

                final int THUMBSIZE = 400;
                bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(overlay.getPhotoPath()), (int) (1.5* THUMBSIZE),THUMBSIZE);
                imageButton.setId(overlay.getOverlayID());
                imageButton.setImageBitmap(bitmap);

                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        intent.putExtra("overlayID", v.getId());
                        startActivity(intent);

                    }

                });

                tableRow.addView(imageButton);
                tableRow.addView(deleteButton);
                table.addView(tableRow);

            }
        }




    public void viewPhoto() {

        Intent intent = new Intent(this, CreateOverlayActivity.class);
        intent.putExtra("currentPhotoPath",currentPhotoPath);
        intent.putExtra("email", email);
        startActivity(intent);

    }


    public void takePhoto(View view){
        dispatchTakePictureIntent();



    }
    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);




            }


        }

    }
    protected void onActivityResult(int requestcode, int resultCode, Intent data) {

        if (requestcode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {

                viewPhoto();
            }
        }
    }
    private File createImageFile()throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents

        currentPhotoPath = image.getAbsolutePath();



        return image;
    }

}
