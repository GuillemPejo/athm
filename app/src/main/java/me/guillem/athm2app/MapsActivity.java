package me.guillem.athm2app;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    float zoomLevel = (float) 16.0;
    float zoomLevelCity = (float) 12.0;
    LatLng BCN = new LatLng(41.38879, 2.15899);
    ViewPager2 viewpager_cards;
    RecyclerAdapterCardsMap recyclerAdapter;

    public static List<Obra> DataCache =new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BCN, zoomLevelCity));

        myRef.child("Obra").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataCache.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Obra obra = ds.getValue(Obra.class);
                        obra.setKey(ds.getKey());
                        DataCache.add(obra);
                    }
                    markMarkers();
                    viewpager_cards = findViewById(R.id.viewpager_cards);
                    //LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
                    //recyclerView.setLayoutManager(layoutManager);
                    //recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerAdapter = new RecyclerAdapterCardsMap(getApplicationContext(),DataCache);
                    viewpager_cards.setAdapter(recyclerAdapter);


                    viewpager_cards.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(DataCache.get(position).lat, DataCache.get(position).lng), zoomLevel));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(DataCache.get(position).lat, DataCache.get(position).lng), zoomLevel));

                        }
                    });
                }else {
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE CRUD", databaseError.getMessage());
            }
        });


    }

    public void markMarkers() {


        for(int i = 0;i<DataCache.size();i++)
        {
            LatLng lat = new LatLng(DataCache.get(i).lat,DataCache.get(i).lng);

            mMap.addMarker(new MarkerOptions().position(lat).title(DataCache.get(i).key).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.icon_map)));
        }


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for(int i = 0;i<DataCache.size();i++)
                {
                    if(marker.getTitle().equals(DataCache.get(i).key)){
                        viewpager_cards.setCurrentItem(i, false);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), zoomLevel));
                        break;
                    }
                }
                return false;
            }
        });

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}