package services;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dtos.CountryDto;
import lk.nsbm.travelsl.R;
import util.CountryRecyclerViewAdapter;

public class CountryService {

    private AppCompatActivity appCompatActivity;
    private RecyclerView recyclerView;
    private CountryRecyclerViewAdapter countryRecyclerViewAdapter;
    private static final String BASE_URL = "http://10.0.2.2:8000/location/";

    public CountryService(AppCompatActivity appCompatActivity, RecyclerView recyclerView) {
        this.appCompatActivity = appCompatActivity;
        this.recyclerView = recyclerView;
    }

    public void getAllCountries(){
        final List<CountryDto> countryDtoList = new ArrayList<>();
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BASE_URL+"get-locations", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response.length() > 0){
                    for (int i = 0; i < response.length() ; i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            CountryDto countryDto = new CountryDto();
                            countryDto.setCountry(jsonObject.getString("country"));
                            countryDto.setCurrency(jsonObject.getString("currency"));
                            countryDto.setDescription(jsonObject.getString("description"));
                            countryDto.setId(jsonObject.getString("id"));
                            countryDto.setImage(jsonObject.getString("image"));
                            countryDto.setPopulation(jsonObject.getLong("population"));
                            countryDtoList.add(countryDto);
                        }catch (JSONException exception){
                            Toast.makeText(appCompatActivity,"Something Went Wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                    countryRecyclerViewAdapter = new CountryRecyclerViewAdapter(appCompatActivity , countryDtoList);
                    recyclerView.setAdapter(countryRecyclerViewAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(appCompatActivity,"Countries Fetching Error",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+appCompatActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("authToken",""));
                headers.put("Accept","application/json");
                return headers;
            }
        };
        Volley.newRequestQueue(appCompatActivity).add(jsonArrayRequest);
    }

    public void getCountryById(String id){
        final TextView countryName = appCompatActivity.findViewById(R.id.countryInfoName);
        final TextView countryCurrency = appCompatActivity.findViewById(R.id.countryInfoCurrency);
        final TextView countryPopulation = appCompatActivity.findViewById(R.id.countryInfoPopulation);
        final ImageView countryImage = appCompatActivity.findViewById(R.id.countryInfoImage);
        JsonObjectRequest countryByIdRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL+"get-country/"+id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    countryName.setText(response.getString("country"));
                    Picasso.get().load(response.getString("image").replace("\"","")).into(countryImage);
                    countryPopulation.setText(response.getLong("population")+"");
                    countryCurrency.setText(response.getString("currency"));
                }catch (JSONException e){
                    Toast.makeText(appCompatActivity,"Country Not Found",Toast.LENGTH_LONG).show();
                    appCompatActivity.finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(appCompatActivity,"Country Not Found",Toast.LENGTH_LONG).show();
                appCompatActivity.finish();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+appCompatActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("authToken",""));
                headers.put("Accept","application/json");
                return headers;
            }
        };
        Volley.newRequestQueue(appCompatActivity).add(countryByIdRequest);
    }
}
