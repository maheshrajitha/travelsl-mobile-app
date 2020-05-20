package services;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class AuthService {

    private static final String TAG = "AuthServiceClass";
    private static final String BASE_URL = "http://10.0.2.2:8000/auth";
    private Application application;

    public AuthService(Application application) {
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, Arrays.toString(error.networkResponse.data));
                Toast.makeText(application.getApplicationContext(),"Wrong Email Or Password",Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this.application).add(loginRequest);
    }
}
