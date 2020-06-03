package services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dtos.LocationDto;
import lk.nsbm.travelsl.R;
import util.LocationRecyclerViewAdapter;

public class LocationService {

    private AppCompatActivity activity;
    private LocationRecyclerViewAdapter locationRecyclerViewAdapter;
    private RecyclerView recyclerView;

    private static final String TAG = "LocationServicesClass";
    private static final String BASE_URL = "http://10.0.2.2:8000/location/";

    public LocationService(AppCompatActivity activity , @Nullable RecyclerView recyclerView) {
        this.activity = activity;
        this.recyclerView = recyclerView;

    }

    public List<LocationDto> getAllLocations(){
        final List<LocationDto> locationDtos = new ArrayList<LocationDto>();
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BASE_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response.length() > 0){
                    for (int i = 0 ; i < response.length() ; i ++){
                        try {
                            JSONObject locationJsonObject = response.getJSONObject(i);
                            LocationDto locationDto = new LocationDto();
                            locationDto.setName(locationJsonObject.get("city").toString());
                            locationDto.setId(locationJsonObject.get("id").toString());
                            locationDto.setImage(locationJsonObject.get("image").toString());
                            locationDtos.add(locationDto);
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
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+activity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("authToken",""));
                headers.put("Accept","application/json");
                return headers;
            }
        };
        Volley.newRequestQueue(activity).add(jsonArrayRequest);
        return locationDtos;
    }

    public void getLocationById(String locationId){
        final TextView locationName = activity.findViewById(R.id.locationNameTextView);
        final ImageView locationImageView = activity.findViewById(R.id.locationImageView);
        final TextView txttNearestAirPort = activity.findViewById(R.id.txtNearestAirport);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + locationId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i(TAG , response.toString());
                    locationName.setText(response.getString("city"));
                    Picasso.get().load(response.getString("image")).into(locationImageView);
                    txttNearestAirPort.setText(response.getString("nearestAirport"));
                    getWeatherInfromation(response.getString("city"));
                    getLocationWikipediaData(response.getString("city"));
                } catch (JSONException e) {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity , "Something Went Wrong",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String> headers = new HashMap<>();
                headers.put("Accept","application/json");
                headers.put("Authorization","Bearer "+activity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("authToken",""));
                return headers;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);
    }

    private void getLocationWikipediaData(String locationName){
        final TextView locationDescriptionTextView = activity.findViewById(R.id.locationDescriptionTextView);
        JsonObjectRequest wikipediaApiRequest = new JsonObjectRequest(Request.Method.GET, "https://en.wikipedia.org/api/rest_v1/page/summary/" + locationName, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    locationDescriptionTextView.setText(response.getString("extract"));
                } catch (JSONException e) {
                    Toast.makeText(activity , "Something Went Wrong",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity , "Something Went Wrong",Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(activity).add(wikipediaApiRequest);
    }

    private void getWeatherInfromation(String city){
        final TextView locationTempTextView = activity.findViewById(R.id.locationTempTextView);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL+"weather/"+city, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i("LocationService", response.toString());
                try{

                    locationTempTextView.setText(response.getString("temprature"));
                    //Picasso.get().load(response.getString("icon").replace("\"","")).into(locationWeatherImageView);
                }catch(JSONException e){
                    Toast.makeText(activity , "Something Went Wrong",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity , "Something Went Wrong",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String> headers = new HashMap<>();
                headers.put("Accept","application/json");
                headers.put("Authorization","Bearer "+activity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("authToken",""));
                return headers;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);
    }

    public void getCordinates(final String city , final MapView mapView){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL+"get-coords/"+city, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                Log.i("ViewOnMapActivity", "onResponse: "+response.toString());
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                        final DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        mapboxMap.setStyle(Style.DARK, new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {
                                SymbolManager symbolManager = new SymbolManager(mapView , mapboxMap ,style);
                                symbolManager.setIconAllowOverlap(true);
                                symbolManager.setIconIgnorePlacement(true);
                                Bitmap marker = BitmapFactory.decodeResource(activity.getResources(),R.drawable.mapbox_marker_icon_default);
                                Objects.requireNonNull(mapboxMap.getStyle()).addImage("marker",marker);
                                try {
                                    Log.i(TAG,response.getDouble("lat")+"");
                                    Symbol symbol = symbolManager.create(new SymbolOptions().withLatLng(new LatLng(Double.parseDouble(decimalFormat.format(response.getDouble("lat"))), Double.parseDouble(decimalFormat.format(response.getDouble("long")))))
                                            .withIconImage("marker").withIconSize(2.0f).withTextField(city).withTextColor("white")
                                    );
                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(Double.parseDouble(decimalFormat.format(response.getDouble("lat"))), Double.parseDouble(decimalFormat.format(response.getDouble("long")))))
                                            .zoom(10).bearing(360).tilt(0).build();
                                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),7000);
                                } catch (JSONException e) {
                                    Log.e(TAG,"JSON Error");
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String> headers = new HashMap<>();
                headers.put("Accept","application/json");
                headers.put("Authorization","Bearer "+activity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("authToken",""));
                return headers;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);
    }
}
