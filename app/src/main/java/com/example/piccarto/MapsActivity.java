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


import java.util.ArrayList;

import static java.lang.Boolean.TRUE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //private FusedLocationProviderClient mFusedLocationProviderClient;
    LatLng sw;
    LatLng ne;
    BitmapDescriptor bitmap;
    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_maps);

        Intent mIntent = getIntent();
        //ArrayList<LatLng> gpsCoords = (ArrayList<LatLng>) mIntent.getSerializableExtra("gps");
       // ArrayList<PointF> coords = (ArrayList<PointF>) mIntent.getSerializableExtra("points");


        String currentPhotoPath = mIntent.getStringExtra("path");
        //String currentPhotoPath = "/Android/data/com.example.piccarto/files/Pictures/test.jpg";
        bitmap = BitmapDescriptorFactory.fromPath(currentPhotoPath);
               Toast toast = Toast.makeText(getApplicationContext(),
                currentPhotoPath,
                Toast.LENGTH_LONG);

        toast.show();
/*
        Double a2 = gpsCoords.get(1).latitude;
        Double a1 = gpsCoords.get(0).latitude;
        Double o2 = gpsCoords.get(1).longitude;
        Double o1 = gpsCoords.get(0).longitude;
        Float x1 = coords.get(0).x;
        Float x2 = coords.get(1).x;
        Float y1 = coords.get(0).y;
        Float y2 = coords.get(1).y;
        Double a0 = (o2 + ((x1*a1)/y1)-o1 +(a2*(x2/y2)))/((x1/y1)-(x2/y2));
        Double o0 = o1-((x1/y1)*(a1-a0));
        Double oe = image.getWidth()*((o2-o1)/(x2-x1)) + o0;
        Double ae = image.getHeight()*((a2-a1)/(y2-y1)) -a0;*/
        sw = new LatLng(39.170892,-94.568726);//a0,o0);
        ne = new LatLng(39.171438, -94.567590);//ae,oe);





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

        LatLngBounds customBounds = new LatLngBounds(sw,ne);
        GroundOverlayOptions customMap = new GroundOverlayOptions()
                .image(bitmap)
                .positionFromBounds(customBounds);
        googleMap.addGroundOverlay(customMap);

        // Add a marker in Sydney and move the camera
        //LatLng rockhurst = new LatLng(39.029251, -94.573347);
        //mMap.addMarker(new MarkerOptions().position(rockhurst).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ne));
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
