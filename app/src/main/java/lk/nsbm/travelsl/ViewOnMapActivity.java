package lk.nsbm.travelsl;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import services.LocationService;

public class ViewOnMapActivity extends AppCompatActivity {

    public static final String TAG = "ViewOnMapActivity";
    private MapView mapView;
    private LocationService locationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_view_on_map);
        Log.i(TAG, "onCreate: "+getIntent().getStringExtra("city"));
        locationService = new LocationService(this,null);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        locationService.getCordinates(getIntent().getStringExtra("city"),mapView);
    }
}
