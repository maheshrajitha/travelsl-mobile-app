package util;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lk.nsbm.travelsl.R;

public class ViewHolder extends RecyclerView.ViewHolder {

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

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        locationNameTextView = itemView.findViewById(R.id.locationNameTextView);
        locationImage = itemView.findViewById(R.id.locationImage);
    }
}
