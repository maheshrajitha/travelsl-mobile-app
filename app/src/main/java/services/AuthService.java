package services;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import lk.nsbm.travelsl.LocationListActivity;

public class AuthService {

    private static final String TAG = "AuthServiceClass";
    private static final String BASE_URL = "http://10.0.2.2:8000/auth";
    private AppCompatActivity application;

    public AuthService(AppCompatActivity application) {
        this.application = application;
    }

    public void login(String email , String password) throws JSONException {
        JSONObject loginObject = new JSONObject();
        loginObject.put("email",email);
        loginObject.put("password",password);
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, loginObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG,response.toString());
                try {
                    Log.i(TAG,response.getString("token"));
                    application.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putString("authToken",response.getString("token")).apply();
                    application.startActivity(new Intent(application, LocationListActivity.class));
                    application.finish();
                } catch (JSONException e) {
                    Toast.makeText(application,"Some Thing Went Wrong",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, Arrays.toString(error.networkResponse.data));
                Toast.makeText(application.getApplicationContext(),"Wrong Email Or Password",Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this.application).add(loginRequest);
    }
}
