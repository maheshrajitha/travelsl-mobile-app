package services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dtos.LocationDto;
import lk.nsbm.travelsl.R;
import util.LocationRecyclerViewAdapter;

public class LocationService {

    private AppCompatActivity activity;
    private LocationRecyclerViewAdapter locationRecyclerViewAdapter;
    private RecyclerView recyclerView;

    private static final String TAG = "LocationServicesClass";
    private static final String BASE_URL = "http://10.0.2.2:8000/locations/";

    public LocationService(AppCompatActivity activity , @Nullable RecyclerView recyclerView) {
        this.activity = activity;
        this.recyclerView = recyclerView;

    }

    public List<LocationDto> getAllLocations(){
        final List<LocationDto> locationDtos = new ArrayList<LocationDto>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BASE_URL+"get-locations/", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response.length() > 0){
                    for (int i = 0 ; i < response.length() ; i ++){
                        try {
                            JSONObject locationJsonObject = response.getJSONObject(i);
                            LocationDto locationDto = new LocationDto();
                            locationDto.setName(locationJsonObject.get("name").toString());
                            locationDto.setDescription(locationJsonObject.get("description").toString());
                            locationDto.setId(locationJsonObject.get("id").toString());
                            List<String> urlList = new ArrayList<String>();
                            for (int a = 0 ; a < locationJsonObject.getJSONArray("images").length() ; a ++){
                                urlList.add(locationJsonObject.getJSONArray("images").get(a).toString());
                            }
                            locationDto.setImages(urlList);
                            locationDtos.add(locationDto);
                            //urlList.clear();
                        } catch (JSONException e) {
                            Log.i("LocationService",e.toString());
                            Toast.makeText(activity,"Something Went Wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                    locationRecyclerViewAdapter = new LocationRecyclerViewAdapter(activity,locationDtos);
                    Log.i(TAG, "onResponse:"+locationDtos.size());
                    recyclerView.setAdapter(locationRecyclerViewAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,"Something Went Wrong",Toast.LENGTH_LONG).show();
                //Log.e(TAG,error.getMessage());
            }
        });
        Volley.newRequestQueue(activity).add(jsonArrayRequest);
        //Log.d(TAG,locationDtos.toString());
        return locationDtos;
    }

    public void getLocationById(String locationId){
        final TextView locationName = activity.findViewById(R.id.locationNameTextView);
        final ImageView locationImageView = activity.findViewById(R.id.locationImageView);
        final TextView locationDescriptionTextView = activity.findViewById(R.id.locationDescriptionTextView);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL +"get-by-id/"+ locationId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    locationName.setText(response.getString("name"));
                    Picasso.get().load(response.getJSONArray("images").getString(0)).into(locationImageView);
                    locationDescriptionTextView.setText(response.getString("description"));
                    getWeatherInfromation(response.getString("city"));
                } catch (JSONException e) {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity , "Something Went Wrong",Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(activity).add(jsonObjectRequest);
    }

    private void getWeatherInfromation(String city){
        final ImageView locationWeatherImageView = activity.findViewById(R.id.locationWeatherImageView);
        final TextView locationTempTextView = activity.findViewById(R.id.locationTempTextView);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL+"get-location-weather/"+city, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    Log.i("LocationService", "onResponse: "+response.getString("icon").replace("\"",""));
                    locationTempTextView.setText(response.getString("temp"));
                    Picasso.get().load(response.getString("icon").replace("\"","")).into(locationWeatherImageView);
                }catch(JSONException e){
                    Toast.makeText(activity , "Something Went Wrong",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity , "Something Went Wrong",Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(activity).add(jsonObjectRequest);
    }

    public void getCordinates(final String city , final MapView mapView){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL+"get-cordinates/"+city, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                Log.i("ViewOnMapActivity", "onResponse: "+response.toString());
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                        mapboxMap.setStyle(Style.DARK, new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {
                                SymbolManager symbolManager = new SymbolManager(mapView , mapboxMap ,style);
                                symbolManager.setIconAllowOverlap(true);
                                symbolManager.setIconIgnorePlacement(true);
                                Bitmap marker = BitmapFactory.decodeResource(activity.getResources(),R.drawable.location_indicator);
                                Objects.requireNonNull(mapboxMap.getStyle()).addImage("marker",marker);
                                try {
                                    Symbol symbol = symbolManager.create(new SymbolOptions().withLatLng(new LatLng(response.getDouble("lat"), response.getDouble("long")))
                                            .withIconImage("marker").withIconSize(2.0f).withTextField(city).withTextColor("white")
                                    );
                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(response.getDouble("lat"), response.getDouble("long")))
                                            .zoom(10).bearing(360).tilt(0).build();
                                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),7000);
                                } catch (JSONException e) {
                                    Toast.makeText(activity , "Something Went Wrong",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity , "Something Went Wrong",Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(activity).add(jsonObjectRequest);
    }
}
