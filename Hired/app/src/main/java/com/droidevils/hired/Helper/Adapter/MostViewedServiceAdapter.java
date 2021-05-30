package com.droidevils.hired.Helper.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidevils.hired.R;

import java.util.ArrayList;

public class MostViewedServiceAdapter extends RecyclerView.Adapter<MostViewedServiceAdapter.ServiceViewHolder> {

    ArrayList<ServiceHelper> featureServices;

    public MostViewedServiceAdapter(ArrayList<ServiceHelper> featureServices) {
        this.featureServices = featureServices;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_viewed_card_design, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceHelper serviceHelper = featureServices.get(position);
        holder.image.setImageResource(serviceHelper.getImage());
        holder.rating.setRating(serviceHelper.getRating());
        holder.title.setText(serviceHelper.getTitle());
        holder.title.setContentDescription(serviceHelper.getId());
        holder.desc.setText(serviceHelper.getDesc());
    }

    @Override
    public int getItemCount() {
        return featureServices.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        RatingBar rating;
        TextView title, desc;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.most_viewed_image);
            rating = itemView.findViewById(R.id.most_viewed_rating);
            title = itemView.findViewById(R.id.most_viewed_title);
            desc = itemView.findViewById(R.id.most_viewed_desc);
        }
    }
}
