package com.example.piccarto;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;


import java.util.ArrayList;

import static java.lang.Boolean.TRUE;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //private FusedLocationProviderClient mFusedLocationPrviderClient;
    LatLng sw, ne, latlng1, latlng2, latlng1a, latlng1o;
    BitmapDescriptor bitmap;
    Bitmap image;
    Float x1, x2, y1, y2, bearing, height, width, dy, dx, scaleR,gpsR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_maps);

        Intent mIntent = getIntent();

        float[] coords = mIntent.getFloatArrayExtra("points");
        double[] gpsCoords = mIntent.getDoubleArrayExtra("gps");

        String currentPhotoPath = mIntent.getStringExtra("path");
        image = BitmapFactory.decodeFile(currentPhotoPath);
        bitmap = BitmapDescriptorFactory.fromPath(currentPhotoPath);

        Double a2 = gpsCoords[2];
        Double a1 = gpsCoords[0];
        Double o2 = gpsCoords[3];
        Double o1 = gpsCoords[1];
        x1 = coords[0];
        x2 = coords[2];
        y1 = coords[1];
        y2 = coords[3];
        dy=y1-y2;
        dx = x2-x1;
        Double G = abs(a2-a1)/(double)abs(y2-y1);
        Double g = abs(o2-o1)/(double)abs(x2-x1);
        Double Asw = a1 - G*(image.getHeight()-(double)y1);
        Double Osw = o1 - g*(double)x1;
        Double Ane = G*(double)y1 +a1;
        Double One = g*(image.getWidth()-(double)x1) +o1;
        sw = new LatLng(Asw,Osw);
        ne = new LatLng(Ane, One);
        latlng1 = new LatLng(a1, o1);
        latlng2 = new LatLng(a2,o2);
        latlng1a = new LatLng(a1+0.1,o1);
        latlng1o = new LatLng(a1,o1+0.1);
        scaleR = (float)SphericalUtil.computeDistanceBetween(latlng1,latlng2)/ (float)sqrt(pow(dx,2)+pow(dy,2));
        //gpsR= (float)SphericalUtil.computeDistanceBetween(latlng1,latlng1a)/(float)SphericalUtil.computeDistanceBetween(latlng1,latlng1o);
        //gpsR represents ratio of a change in latitude to an equal change in longitude (used to scale x and y differing amounts)


        Double heading = SphericalUtil.computeHeading(latlng1,latlng2);
        //Double slope = (double)((x2-x1)/(y1-y2)); // y coordinates reversed due to reversal of y direction for photos
        Float photoHeading = (float) ((atan2(dy,dx) * 180) / PI);

        bearing = (float) (heading + photoHeading + 270 +720)%360;
       // height = scaleR*image.getHeight()*(float)(1+(gpsR-1)*sin(bearing));
       // width = scaleR*image.getWidth()*(float)(1+(1/gpsR-1)*cos(bearing));
        width = scaleR*image.getWidth();

  /*      Toast toast = Toast.makeText(getApplicationContext(),
                Asw.toString() + ", " + Osw.toString() +":"+ Ane.toString()+", "+ One.toString()+", "+ x1.toString()+", "+ x2.toString(),
                Toast.LENGTH_LONG);

        toast.show();*/





        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

/*        FloatingActionButton fabAddMap = findViewById(R.id.fabAddMap);
        fabAddMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, ImagActivity.class);
                startActivity(intent);
            }
        });*/
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
       // mMap = googleMap;

      //  LatLngBounds customBounds = new LatLngBounds(sw, ne);
        GroundOverlayOptions customMap = new GroundOverlayOptions()
                .image(bitmap)
                .anchor(x1/image.getWidth(), y1/image.getHeight())
                .bearing(bearing)
                .position(latlng1,width);
                //,height);

                //.positionFromBounds(customBounds);
        googleMap.addGroundOverlay(customMap);
        Toast toast = Toast.makeText(getApplicationContext(),
                "x1:" + x1.toString() + ",     " + "y1:  " + y1.toString() +",     Bearing: "+ bearing.toString(), Toast.LENGTH_LONG);

        toast.show();

        // Add a marker in Sydney and move the camera
        //LatLng rockhurst = new LatLng(39.029251, -94.573347);
        //mMap.addMarker(new MarkerOptions().position(rockhurst).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng1));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(TRUE);
    }


}
