package lk.nsbm.travelsl;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dtos.LocationDto;
import services.LocationService;
import util.LocationRecyclerViewAdapter;

public class LocationListActivity extends AppCompatActivity {

    List<LocationDto> locationDtoList = new ArrayList<LocationDto>();
    List<String> url = new ArrayList<String>();
    RecyclerView locationListRecycleView;
    LocationRecyclerViewAdapter locationRecyclerViewAdapter;
    private LocationService locationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        locationListRecycleView = findViewById(R.id.locationListRecyclerView);
        locationListRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        locationService = new LocationService(this ,locationListRecycleView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        locationService.getAllLocations();
    }
}
