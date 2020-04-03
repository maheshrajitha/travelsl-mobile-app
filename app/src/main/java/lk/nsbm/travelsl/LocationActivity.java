package lk.nsbm.travelsl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import dtos.LocationDto;
import services.LocationService;

public class LocationActivity extends AppCompatActivity {

    private LocationDto locationDto;
    private TextView locationName;
    private TextView locationCity;
    private LocationService locationService = new LocationService(this, null);
    private ImageView locationImage;
    private static final String TAG = "LocationActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Log.d(TAG, "onCreate: "+getIntent().getStringExtra("locationId"));

    }

    @Override
    protected void onStart() {
        super.onStart();
        locationService.getLocationById(getIntent().getStringExtra("locationId"));
    }
}
