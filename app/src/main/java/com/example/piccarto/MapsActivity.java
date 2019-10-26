package com.example.piccarto;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
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

import androidx.room.Room;

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
    LatLng latlng1;
    BitmapDescriptor bitmap;
    Bitmap image;
    Float  bearing, width;
    Overlay overlay;
    String currentPhotoPath;
    OverlayDao overlayDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "overlay-database")
                .allowMainThreadQueries()
                .build();
        overlayDao = database.overlayDao();
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_maps);

        Intent mIntent = getIntent();
        int overlayID = mIntent.getIntExtra("overlayID",0);
        overlay = overlayDao.findById(overlayID);
        currentPhotoPath = overlay.getPhotoPath();

        image = BitmapFactory.decodeFile(currentPhotoPath);
        bitmap = BitmapDescriptorFactory.fromPath(currentPhotoPath);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton homeButton = findViewById(R.id.fabHome);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                intent.putExtra("email", overlay.getOwner());
                startActivity(intent);
            }
        });
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
        latlng1 = new LatLng(overlay.getPositionA(),overlay.getPosition0());
       // mMap = googleMap;

      //  LatLngBounds customBounds = new LatLngBounds(sw, ne);
        GroundOverlayOptions customMap = new GroundOverlayOptions()
                .image(bitmap)
                .anchor(overlay.getAnchorX(), overlay.getAnchorY())
                .bearing(overlay.getBearing())
                .position(latlng1,overlay.getWidth());
                //,height);
                //.positionFromBounds(customBounds);
        googleMap.addGroundOverlay(customMap);

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
