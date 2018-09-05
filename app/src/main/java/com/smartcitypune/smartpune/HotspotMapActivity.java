package com.smartcitypune.smartpune;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class HotspotMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private DatabaseReference mDatabase;
    private ArrayList<WeightedLatLng> list;
    private int dayOfWeek;
    private int hourOfDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotspot_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap = googleMap;

        Calendar calendar = Calendar.getInstance();
        dayOfWeek = 2;
//        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        hourOfDay = 13;
//        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);


        LatLng userLocation = new LatLng(51.5503, -0.1095);
        mMap.addMarker(new MarkerOptions().position(userLocation).title("You"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        updateHeatPoints();
    }

    public void displayHeatMap() {
        mProvider = new HeatmapTileProvider.Builder().weightedData(list).build();
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private void updateHeatPoints() {
        list = new ArrayList<WeightedLatLng>();
        mDatabase = FirebaseDatabase.getInstance().getReference("/road-accidents");

        ValueEventListener pointListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userDataSnapshot : dataSnapshot.getChildren()) {
                    Point newPoint = userDataSnapshot.getValue(Point.class);
                    Log.d("sg", "onDataChange: "+newPoint.toString());
                    if (newPoint.getDay_of_week() == dayOfWeek && newPoint.getTime() == hourOfDay) {
                        WeightedLatLng weightedLatLng = new WeightedLatLng(new LatLng(newPoint.getLatitude(), newPoint.getLongitude()), newPoint.getCount());
                        list.add(weightedLatLng);
                        Log.d("sdgse", "AFSFSG");
                    }
                }
                Log.d("SDFg", list.size() + " List: " + list);
                displayHeatMap();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("MapsActivity", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(pointListener);

    }

}
