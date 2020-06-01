package lk.nsbm.travelsl;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import dtos.LocationDto;
import services.LocationService;
import util.LocationRecyclerViewAdapter;

public class LocationListActivity extends AppCompatActivity {

    private List<LocationDto> locationDtoList = new ArrayList<LocationDto>();
    private List<String> url = new ArrayList<String>();
    private RecyclerView locationListRecycleView;
    private LocationRecyclerViewAdapter locationRecyclerViewAdapter;
    private LocationService locationService;
    private BottomNavigationView bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        locationListRecycleView = findViewById(R.id.locationListRecyclerView);
        locationListRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        bottomNavigationBar = findViewById(R.id.bottom_nav_bar);
        locationService = new LocationService(this ,locationListRecycleView);
        bottomNavigationBar.setSelectedItemId(R.id.locationsTab);
        bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.countriesTab:
                        startActivity(new Intent(getApplicationContext(),CountryActivity.class));
                        finish();
                        return true;
                    case R.id.favouritesTab:
                        startActivity(new Intent(getApplicationContext(),FavouritesActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationService.getAllLocations();
    }




}
