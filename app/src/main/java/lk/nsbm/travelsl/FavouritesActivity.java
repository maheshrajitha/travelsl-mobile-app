package lk.nsbm.travelsl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import services.UserService;

public class FavouritesActivity extends AppCompatActivity {

    private UserService userService;
    private BottomNavigationView bottomNavigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        userService = new UserService(this);
        bottomNavigationBar = findViewById(R.id.bottom_nav_bar);
        bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.countriesTab:
                        startActivity(new Intent(getApplicationContext(),CountryActivity.class));
                        finish();
                        return true;
                    case R.id.locationsTab:
                        startActivity(new Intent(getApplicationContext(),LocationListActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userService.getAllFavouriteLocations();
    }
}