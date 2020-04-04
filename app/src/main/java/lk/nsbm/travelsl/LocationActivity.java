package lk.nsbm.travelsl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import dtos.LocationDto;
import services.LocationService;

public class LocationActivity extends AppCompatActivity {

    private LocationService locationService = new LocationService(this, null);
    private static final String TAG = "LocationActivity";
    private TextView locationNameTextView;
    private Button viewOnMapButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        viewOnMapButton = findViewById(R.id.btnViewOnMap);
        locationNameTextView = findViewById(R.id.locationNameTextView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationService.getLocationById(getIntent().getStringExtra("locationId"));
        viewOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LocationActivity.this,ViewOnMapActivity.class).putExtra("city",locationNameTextView.getText()));
                finish();
            }
        });
    }
}
