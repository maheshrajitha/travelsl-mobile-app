package services;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
                                Log.i(TAG, "onResponse:"+locationJsonObject.getJSONArray("images").get(a).toString());
                                urlList.add(locationJsonObject.getJSONArray("images").get(a).toString());
                            }
                            locationDto.setImages(urlList);
                            locationDtos.add(locationDto);
                            //urlList.clear();
                        } catch (JSONException e) {
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
}
