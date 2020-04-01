package services;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dtos.LocationDto;
import util.LocationRecyclerViewAdapter;

public class LocationService {

    private AppCompatActivity activity;
    private LocationRecyclerViewAdapter locationRecyclerViewAdapter;
    private RecyclerView recyclerView;

    private static final String TAG = "LocationServicesClass";
    private static final String BASE_URL = "http://10.0.2.2:8000/locations/";

    public LocationService(AppCompatActivity activity , RecyclerView recyclerView) {
        this.activity = activity;
        this.recyclerView = recyclerView;

    }

    public List<LocationDto> getAllLocations(){
        final List<LocationDto> locationDtos = new ArrayList<LocationDto>();
        final List<String> urlList = new ArrayList<String>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BASE_URL+"get-locations/", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.v(TAG, response.toString());
                if(response.length() > 0){
                    for (int i = 0 ; i < response.length() ; i ++){
                        try {
                            JSONObject locationJsonObject = response.getJSONObject(i);
                            LocationDto locationDto = new LocationDto();
                            locationDto.setName(locationJsonObject.get("name").toString());
                            locationDto.setDescription(locationJsonObject.get("description").toString());
                            locationDto.setId(locationJsonObject.get("id").toString());
                            urlList.clear();
                            for (int a = 0 ; a < locationJsonObject.getJSONArray("images").length() ; a ++){
                                urlList.add(locationJsonObject.getJSONArray("images").get(a).toString().replace("\"",""));
                            }
                            locationDto.setImages(urlList);
                            locationDtos.add(locationDto);
                            locationRecyclerViewAdapter = new LocationRecyclerViewAdapter(activity,locationDtos);
                            recyclerView.setAdapter(locationRecyclerViewAdapter);
                            Log.i(TAG,""+recyclerView.getAdapter().getItemCount());
                        } catch (JSONException e) {
                            Log.e(TAG , e.getMessage());
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,error.getMessage());
            }
        });
        Volley.newRequestQueue(activity).add(jsonArrayRequest);
        Log.d(TAG,locationDtos.toString());
        return locationDtos;
    }
}
