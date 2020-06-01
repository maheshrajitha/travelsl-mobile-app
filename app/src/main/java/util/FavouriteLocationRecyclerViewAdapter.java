package util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import dtos.FavouriteLocationDto;
import lk.nsbm.travelsl.R;

public class FavouriteLocationRecyclerViewAdapter extends RecyclerView.Adapter<FavouriteLocationRecyclerViewAdapter.FavouriteLocationViewHolder> {


    private List<FavouriteLocationDto> favouriteLocationDtos;
    private LayoutInflater layoutInflater;

    public FavouriteLocationRecyclerViewAdapter(List<FavouriteLocationDto> favouriteLocationDtos, Context context) {
        this.favouriteLocationDtos = favouriteLocationDtos;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FavouriteLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavouriteLocationViewHolder(layoutInflater.inflate(R.layout.favourite_location_card,parent,false),favouriteLocationDtos);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteLocationViewHolder holder, int position) {
        holder.getTxtFavouriteLocationName().setText(favouriteLocationDtos.get(position).getCity());
        Picasso.get().load(favouriteLocationDtos.get(position).getImage().replace("\"","").replace(" ","")).into(holder.getImgFavouriteLocationImageView());
    }

    @Override
    public int getItemCount() {
        return this.favouriteLocationDtos.size();
    }

    public class FavouriteLocationViewHolder extends RecyclerView.ViewHolder {


        private TextView txtFavouriteLocationName;
        private ImageView imgFavouriteLocationImageView;

        private List<FavouriteLocationDto> favouriteLocationDtos;

        public FavouriteLocationViewHolder(View view , List<FavouriteLocationDto> favouriteLocationDtos){
            super(view);
            this.txtFavouriteLocationName = view.findViewById(R.id.favouriteLocationNameTextView);
            this.imgFavouriteLocationImageView = view.findViewById(R.id.favouriteLocationImage);
            this.favouriteLocationDtos = favouriteLocationDtos;
        }

        public TextView getTxtFavouriteLocationName() {
            return txtFavouriteLocationName;
        }

        public void setTxtFavouriteLocationName(TextView txtFavouriteLocationName) {
            this.txtFavouriteLocationName = txtFavouriteLocationName;
        }

        public ImageView getImgFavouriteLocationImageView() {
            return imgFavouriteLocationImageView;
        }

        public void setImgFavouriteLocationImageView(ImageView imgFavouriteLocationImageView) {
            this.imgFavouriteLocationImageView = imgFavouriteLocationImageView;
        }
    }
}
