package lk.nsbm.travelsl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import services.CountryService;

public class CountryInfoActivity extends AppCompatActivity {

    private CountryService countryService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_info);
        countryService = new CountryService(this,null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        countryService.getCountryById(getIntent().getStringExtra("countrId"));
    }
}