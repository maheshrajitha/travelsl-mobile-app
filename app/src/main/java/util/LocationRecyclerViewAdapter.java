package util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import dtos.LocationDto;
import lk.nsbm.travelsl.R;

public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<LocationDto> locationDtoList;

    public LocationRecyclerViewAdapter(Context context, List<LocationDto> locationDtoList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.locationDtoList = locationDtoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.location_list,parent,false);
        return new ViewHolder(view , locationDtoList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("LocationSer", "onBindViewHolder: "+locationDtoList.size());
        holder.getLocationNameTextView().setText(String.format("%s%s", locationDtoList.get(position).getName().substring(0, 1).toUpperCase(), locationDtoList.get(position).getName().substring(1)));
        Log.i("Picssa",locationDtoList.get(position).getImages());
        Picasso.get().load(locationDtoList.get(position).getImages().replace("\"","").replace(" ","")).into(holder.getLocationImage());

    }


    @Override
    public int getItemCount() {
        return locationDtoList.size();
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public List<LocationDto> getLocationDtoList() {
        return locationDtoList;
    }

    public void setLocationDtoList(List<LocationDto> locationDtoList) {
        this.locationDtoList = locationDtoList;
    }
}
