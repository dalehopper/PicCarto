package com.example.piccarto;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import android.graphics.Matrix;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.Manifest;

import androidx.room.Room;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class CreateOverlayActivity extends AppCompatActivity implements Serializable {
    Float x1, x2, y1, y2, bearing, height, width, dy, dx, scaleR,gpsR;
    LatLng latlng1;
    LatLng latlng2;
    //Bitmap image;
    double longitude;
    double latitude;

    int nextIndex = 0, overlayID;
    List<LatLng> latLngs = new ArrayList<LatLng>();
    double[] gpsCoords = new double[4];
    PointF knownPoint = new PointF();
    String currentPhotoPath;
    ImageView imageView;
    float[] coords = new float[4];
    float[] values = new float[9];
    int pointCount = 0;


    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();


    int mode = NONE;


    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    PointF tempPoint = new PointF();
    float oldDist = 1f;



    LocationManager locationManager;
    Context mContext;
    OverlayDao overlayDao;
    Bitmap bitmap;
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=1338;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "overlay-database")
                .allowMainThreadQueries()
                .build();
        overlayDao = database.overlayDao();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        if (!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }
        imageView = findViewById(R.id.mimageView);

        Intent mIntent = getIntent();
        currentPhotoPath = mIntent.getStringExtra("currentPhotoPath");
        bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        imageView.setImageBitmap(bitmap);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;


                // Dump touch event to log

                // Handle touch events here...
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        mode = DRAG;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        long clickDuration = event.getEventTime() - event.getDownTime();
                        if (clickDuration < 200) {
                            //click event has occurred
                            getPixel(event, view, tempPoint);



                            if (coords[3] >0) {
                                x1 = coords[0];
                                x2 = coords[2];
                                y1 = coords[1];
                                y2 = coords[3];
                                dy=y1-y2;
                                dx = x2-x1;
                                latlng1 = latLngs.get(0);
                                latlng2 = latLngs.get(1);
                                Double heading = SphericalUtil.computeHeading(latlng1,latlng2);
                                //Double slope = (double)((x2-x1)/(y1-y2)); // y coordinates reversed due to reversal of y direction for photos
                                Float photoHeading = (float) ((atan2(dy,dx) * 180) / PI);

                                bearing = (float) (heading + photoHeading + 270 +720)%360;
                                // height = scaleR*image.getHeight()*(float)(1+(gpsR-1)*sin(bearing));
                                // width = scaleR*image.getWidth()*(float)(1+(1/gpsR-1)*cos(bearing));
                                scaleR = (float) (SphericalUtil.computeDistanceBetween(latlng1,latlng2)/sqrt(pow(dx,2)+pow(dy,2)));
                                width = scaleR*bitmap.getWidth();
                                Overlay overlay = new Overlay();
                                overlay.setPhotoPath(currentPhotoPath);
                                overlay.setBearing(bearing);
                                overlay.setWidth(width);
                                overlay.setAnchorX(x1/bitmap.getWidth());
                                overlay.setAnchorY(y1/bitmap.getHeight());
                                overlay.setPosition0(latlng1.longitude);
                                overlay.setPositionA(latlng1.latitude);

                                ;
                                Intent intent = new Intent(CreateOverlayActivity.this, MapsActivity.class);
                                intent.putExtra("overlayID", (int) overlayDao.insert(overlay));
                                /*intent.putExtra("points", coords);
                                intent.putExtra("gps", gpsCoords);
                                intent.putExtra("path", currentPhotoPath);*/

                                startActivity(intent);
                            }
                            break;

                        }

                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            // ...
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = newDist / oldDist;
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                        }
                        break;
                }

                view.setImageMatrix(matrix);
                return true; // indicate event was handled
            }


            /**
             * Determine the space between the first two fingers
             */
            private float spacing(MotionEvent event) {
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                return (float) Math.sqrt(x * x + y * y);
            }

            /**
             * Calculate the mid point of the first two fingers
             */
            private void midPoint(PointF point, MotionEvent event) {
                float x = event.getX(0) + event.getX(1);
                float y = event.getY(0) + event.getY(1);
                point.set(x / 2, y / 2);
            }

            private void getPixel(MotionEvent event, ImageView imageView, PointF point) {
                // Get the values of the matrix
                matrix.getValues(values);

                // values[2] and values[5] are the x,y coordinates of the top left corner of the drawable image, regardless of the zoom factor.
                // values[0] and values[4] are the zoom factors for the image's width and height respectively. If you zoom at the same factor, these should both be the same value.

                // event is the touch event for MotionEvent.ACTION_UP
                float relativeX = (event.getX() - values[2]) / values[0];
                float relativeY = (event.getY() - values[5]) / values[4];


                Toast toast = Toast.makeText(getApplicationContext(),
                        relativeX + "," + relativeY + " ; " + latitude + ", " + longitude,
                        Toast.LENGTH_LONG);

                toast.show();
                LatLng loc;
                loc = new LatLng(latitude,longitude);
                latLngs.add(loc);
               // gpsCoords[nextIndex]=(latitude);
                //gpsCoords[nextIndex+1]=(longitude);
                coords[nextIndex]=(relativeX);
                coords[nextIndex+1]=(relativeY);
                nextIndex+=2;

            }
        });

        mContext=this;
        locationManager=(LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                1000,
                2, locationListenerGPS);
        isLocationEnabled();

    }
    LocationListener locationListenerGPS=new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            latitude=location.getLatitude();
            longitude=location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    protected void onResume(){
        super.onResume();
        isLocationEnabled();
    }
    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }
    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }

    private void isLocationEnabled() {

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
        else{
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Confirm Location");
            alertDialog.setMessage("Your Location is enabled, please enjoy");
            alertDialog.setNegativeButton("Back to interface",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
    }


}


