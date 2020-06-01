package lk.nsbm.travelsl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import services.CountryService;

public class CountryActivity extends AppCompatActivity {

    private CountryService countryService;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        recyclerView = findViewById(R.id.countryListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        countryService = new CountryService(this,recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        countryService.getAllCountries();
    }
}