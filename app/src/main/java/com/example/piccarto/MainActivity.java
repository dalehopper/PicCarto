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
import android.view.View;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "overlay-database")
                .allowMainThreadQueries()
                .build();
        overlayDao = database.overlayDao();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TableLayout table = findViewById(R.id.main_table);
        int i = overlayDao.getCount();
            while ( i > 0) {
                Overlay overlay = overlayDao.findById(i);

                LinearLayout.LayoutParams tableRowParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(tableRowParams);
                ImageButton imageButton = new ImageButton(this);
                final int THUMBSIZE = 500;
                bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(overlay.getPhotoPath()), (int) (1.5* THUMBSIZE),THUMBSIZE);
                imageButton.setId(i-1);
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
                table.addView(tableRow);
                i = i -1;
            }
        }




    public void viewPhoto(int id) {

        Intent intent = new Intent(this, CreateOverlayActivity.class);
        intent.putExtra("currentPhotoPath",currentPhotoPath);
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
                Overlay overlay = new Overlay();
                overlay.setPhotoPath(currentPhotoPath);

                viewPhoto((int) overlayDao.insert(overlay));
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
