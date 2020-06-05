package util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import dtos.CountryDto;
import lk.nsbm.travelsl.CountryInfoActivity;
import lk.nsbm.travelsl.R;

public class CountryRecyclerViewAdapter extends RecyclerView.Adapter<CountryRecyclerViewAdapter.CountryViewHolder> {

    private LayoutInflater layoutInflater;
    private List<CountryDto> countryDtoList;

    public CountryRecyclerViewAdapter(Context context, List<CountryDto> countryDtoList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.countryDtoList = countryDtoList;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.country_layout,parent,false);
        return new CountryViewHolder(view , countryDtoList);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryRecyclerViewAdapter.CountryViewHolder holder, int position) {
        holder.getCountryNameTextView().setText(countryDtoList.get(position).getCountry());
        Log.i("CountryRecyclerViewAda",countryDtoList.get(position).getImage());
        Picasso.get().load(countryDtoList.get(position).getImage().replace("\"","").replace(" ","")).into(holder.getCountryImageView());
    }


    @Override
    public int getItemCount() {
        return countryDtoList.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder{

        private List<CountryDto> countryDtos;
        private ImageView countryImageView;
        private TextView countryNameTextView;
        private TextView countryIdTextView;

        public CountryViewHolder(@NonNull final View itemView , final List<CountryDto> countryDtos) {
            super(itemView);
            this.countryDtos = countryDtos;
            this.countryImageView = itemView.findViewById(R.id.countryImage);
            this.countryNameTextView = itemView.findViewById(R.id.countryNameTextView);
            this.countryIdTextView = itemView.findViewById(R.id.countryIdTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), CountryInfoActivity.class).putExtra("countrId",countryDtos.get(getAdapterPosition()).getId()));
                }
            });
        }

        public ImageView getCountryImageView() {
            return countryImageView;
        }

        public void setCountryImageView(ImageView countryImageView) {
            this.countryImageView = countryImageView;
        }

        public TextView getCountryNameTextView() {
            return countryNameTextView;
        }

        public void setCountryNameTextView(TextView countryNameTextView) {
            this.countryNameTextView = countryNameTextView;
        }

        public TextView getCountryIdTextView() {
            return countryIdTextView;
        }

        public void setCountryIdTextView(TextView countryIdTextView) {
            this.countryIdTextView = countryIdTextView;
        }
    }
}
