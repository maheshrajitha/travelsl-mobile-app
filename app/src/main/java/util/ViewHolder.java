package util;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import lk.nsbm.travelsl.LocationActivity;
import lk.nsbm.travelsl.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    private TextView locationIdTextView;

    public TextView getLocationIdTextView() {
        return locationIdTextView;
    }

    public void setLocationIdTextView(TextView locationIdTextView) {
        this.locationIdTextView = locationIdTextView;
    }

    public TextView getLocationNameTextView() {
        return locationNameTextView;
    }

    public void setLocationNameTextView(TextView locationNameTextView) {
        this.locationNameTextView = locationNameTextView;
    }

    private TextView locationNameTextView;

    public ImageView getLocationImage() {
        return locationImage;
    }

    public void setLocationImage(ImageView locationImage) {
        this.locationImage = locationImage;
    }

    private ImageView locationImage;

    public ViewHolder(@NonNull final View itemView , final AppCompatActivity appCompatActivity) {
        super(itemView);
        locationNameTextView = itemView.findViewById(R.id.locationNameTextView);
        locationImage = itemView.findViewById(R.id.locationImage);
        locationIdTextView = itemView.findViewById(R.id.locationIdTextView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appCompatActivity.startActivity(new Intent(appCompatActivity, LocationActivity.class).putExtra("locationId",locationIdTextView.getText().toString()));
            }
        });
    }


}
