package lk.nsbm.travelsl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import services.CountryService;

public class CountryActivity extends AppCompatActivity {

    private CountryService countryService;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        recyclerView = findViewById(R.id.countryListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        countryService = new CountryService(this,recyclerView);
        bottomNavigationBar = findViewById(R.id.bottom_nav_bar);
        bottomNavigationBar.setSelectedItemId(R.id.locationsTab);
        bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.locationsTab:
                        startActivity(new Intent(getApplicationContext(),LocationListActivity.class));
                        finish();
                        return true;
                    case R.id.favouritesTab:
                        startActivity(new Intent(getApplicationContext(),FavouritesActivity.class));
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
        countryService.getAllCountries();
    }
}