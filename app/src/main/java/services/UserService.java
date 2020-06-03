package services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dtos.FavouriteLocationDto;
import lk.nsbm.travelsl.R;
import util.FavouriteLocationRecyclerViewAdapter;

public class UserService {

    private AppCompatActivity appCompatActivity;
    private RecyclerView favouriteLocationsRecyclerView;
    private FavouriteLocationRecyclerViewAdapter favouriteLocationRecyclerViewAdapter;

    private Context context;
    private static final String BASE_URL = "http://10.0.2.2:8000/location/";

    public UserService(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
        this.favouriteLocationsRecyclerView = appCompatActivity.findViewById(R.id.favouriteLocationListRecyclerView);
        this.favouriteLocationsRecyclerView.setLayoutManager(new LinearLayoutManager(appCompatActivity));
    }

    public UserService (Context context){
        this.context = context;
    }

    public void getAllFavouriteLocations(){
        final List<FavouriteLocationDto> favouriteLocationDtos = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BASE_URL+"get-favourites", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("UserService",response.toString());
                if(response.length() > 0)
                    for(int i = 0 ; i < response.length();i++ ){
                        try{
                            JSONObject jsonObject = response.getJSONObject(i);
                            favouriteLocationDtos.add(new FavouriteLocationDto(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("userId"),
                                    jsonObject.getString("locationId"),
                                    jsonObject.getString("image"),
                                    jsonObject.getString("country"),
                                    jsonObject.getString("city")
                                    ));
                        }catch (JSONException e){
                            Toast.makeText(appCompatActivity,"Something Went Wrong",Toast.LENGTH_LONG).show();
                        }
                    }

                favouriteLocationRecyclerViewAdapter = new FavouriteLocationRecyclerViewAdapter(favouriteLocationDtos,appCompatActivity);
                    favouriteLocationsRecyclerView.setAdapter(favouriteLocationRecyclerViewAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(appCompatActivity,"Favourite Locations Getting Error",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String> headers = new HashMap<>();
                headers.put("Accept","application/json");
                headers.put("Authorization","Bearer "+appCompatActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("authToken",""));
                return headers;
            }
        };
        Volley.newRequestQueue(appCompatActivity).add(jsonArrayRequest);
    }

    public void addToFavourites(String locationId){
        try{
            JSONObject favouriteLocationJson = new JSONObject();
            favouriteLocationJson.put("locationId",locationId);
            JsonObjectRequest saveFavouriteLocationRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL+"add-to-favourites", favouriteLocationJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(context,"Saved To Your Favourite Locations",Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context,"This Location Already In Your Favourite Locations",Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String , String> headers = new HashMap<>();
                    headers.put("Accept","application/json");
                    headers.put("Authorization","Bearer "+context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("authToken",""));
                    return headers;
                }
            };
            Volley.newRequestQueue(context).add(saveFavouriteLocationRequest);
        }catch (JSONException e){
            Toast.makeText(context,"Something Went Wrong",Toast.LENGTH_LONG).show();
        }
    }
}
