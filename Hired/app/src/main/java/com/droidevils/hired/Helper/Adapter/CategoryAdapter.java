package com.droidevils.hired.Helper.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidevils.hired.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    ArrayList<CategoryHelper> categories;

    public CategoryAdapter(ArrayList<CategoryHelper> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_design, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryHelper categoryHelper = categories.get(position);

        holder.image.setImageResource(categoryHelper.getImage());
        holder.title.setText(categoryHelper.getTitle());
        holder.layout.setBackground(categoryHelper.getBackground());
        holder.layout.setContentDescription(categoryHelper.getId());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout layout;
        ImageView image;
        TextView title;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.category_card);
            image = itemView.findViewById(R.id.category_image);
            title = itemView.findViewById(R.id.category_title);

        }
    }

}