package lk.nsbm.travelsl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import services.UserService;

public class FavouritesActivity extends AppCompatActivity {

    private UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        userService = new UserService(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userService.getAllFavouriteLocations();
    }
}