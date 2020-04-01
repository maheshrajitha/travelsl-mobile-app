package util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getLocationNameTextView().setText(String.format("%s%s", locationDtoList.get(position).getName().substring(0, 1).toUpperCase(), locationDtoList.get(position).getName().substring(1)));
        Picasso.get().load(locationDtoList.get(position).getImages().get(0).replace("\"","")).into(holder.getLocationImage());
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
