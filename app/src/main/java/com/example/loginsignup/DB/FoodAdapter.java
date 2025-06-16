package com.example.loginsignup.DB;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.loginsignup.Activity.FoodDetailActivity;
import com.example.loginsignup.R;
import java.io.File;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private static List<Food> foodList;

    public FoodAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.tvFoodName.setText(food.getFoodName());

        //Ảnh
        String imagePath = food.getFoodImage();
        if (imagePath.startsWith("/")) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                holder.imgFood.setImageBitmap(bitmap);
            } else {
                holder.imgFood.setImageResource(R.drawable.placeholder);
            }
        } else {
            int resId = holder.itemView.getResources().getIdentifier(
                    imagePath.replace(".jpg", "").replace(".png", ""),
                    "drawable",
                    holder.itemView.getContext().getPackageName()
            );
            holder.imgFood.setImageResource(resId != 0 ? resId : R.drawable.placeholder);
        }

        //Yêu thích
        SharedPreferences prefs = holder.itemView.getContext().getSharedPreferences("favorites", Context.MODE_PRIVATE);
        boolean isFavorite = prefs.getBoolean("food_" + food.getFoodId(), false);
        holder.ivFavorite.setImageResource(isFavorite ? R.drawable.star_filled : R.drawable.star_outline);

        holder.ivFavorite.setOnClickListener(v -> {
            boolean isFav = !isFavorite;
            prefs.edit().putBoolean("food_" + food.getFoodId(), isFav).apply();
            holder.ivFavorite.setImageResource(isFav ? R.drawable.star_filled : R.drawable.star_outline);
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood, ivFavorite;
        TextView tvFoodName;

        public FoodViewHolder(View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Food food = FoodAdapter.foodList.get(position);
                    Intent intent = new Intent(v.getContext(), FoodDetailActivity.class);
                    intent.putExtra("food", food);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
