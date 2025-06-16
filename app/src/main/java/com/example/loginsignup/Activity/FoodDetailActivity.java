package com.example.loginsignup.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.loginsignup.DB.Food;
import com.example.loginsignup.DB.FoodCRUD;
import com.example.loginsignup.DB.FoodDetail;
import com.example.loginsignup.R;
import java.io.File;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



public class FoodDetailActivity extends AppCompatActivity {
    private FoodCRUD foodCRUD;
    private Food food;
    private FoodDetail foodDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail);

//        getWindow().setFlags(
//                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
//        );

        foodCRUD = new FoodCRUD(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        food = (Food) getIntent().getSerializableExtra("food");
        if (food == null) {
            finish();
            return;
        }

        foodDetail = foodCRUD.getFoodDetailInfo(food.getFoodId());
        if (foodDetail == null) {
            foodDetail = new FoodDetail(food.getFoodId(), "", "","");
        }

        //Yeu thich
        ImageView ivFavorite = findViewById(R.id.ivFavorite);
        SharedPreferences prefs = getSharedPreferences("favorites", MODE_PRIVATE);
        boolean isFavorite = prefs.getBoolean("food_" + food.getFoodId(), false);
        ivFavorite.setImageResource(isFavorite ? R.drawable.star_filled : R.drawable.star_outline);

        ivFavorite.setOnClickListener(v -> {
            boolean isFav = !isFavorite;
            prefs.edit().putBoolean("food_" + food.getFoodId(), isFav).apply();
            ivFavorite.setImageResource(isFav ? R.drawable.star_filled : R.drawable.star_outline);
        });

        ImageView imgFood = findViewById(R.id.imgFoodDetail);
        TextView tvName = findViewById(R.id.tvFoodNameDetail);
        TextView tvCategory = findViewById(R.id.tvCategoryDetail);
        TextView tvDescription = findViewById(R.id.tvDescriptionDetail);
        TextView tvMaterial = findViewById(R.id.tvMaterial);
        TextView tvRecipe = findViewById(R.id.tvRecipe);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);

        String imagePath = food.getFoodImage();
        if (imagePath.startsWith("/")) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                imgFood.setImageBitmap(bitmap);
            } else {
                imgFood.setImageResource(R.drawable.placeholder);
            }
        } else { 
            int resId = getResources().getIdentifier(
                    imagePath.replace(".jpg", "").replace(".png", ""),
                    "drawable",
                    getPackageName()
            );
            imgFood.setImageResource(resId != 0 ? resId : R.drawable.placeholder);
        }


        tvName.setText(food.getFoodName());
        tvCategory.setText("Danh mục: " + getCategoryNameById(food.getCategoryId()));
        tvDescription.setText(foodDetail.getDescription());
        tvMaterial.setText(foodDetail.getMaterial());
        tvRecipe.setText(foodDetail.getRecipe());

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(FoodDetailActivity.this, EditFoodActivity.class);
            intent.putExtra("foodId", food.getFoodId());
            intent.putExtra("foodName", food.getFoodName());
            intent.putExtra("description", foodDetail.getDescription());
            intent.putExtra("material", foodDetail.getMaterial());
            intent.putExtra("recipe", foodDetail.getRecipe());
            intent.putExtra("categoryId", food.getCategoryId());
            intent.putExtra("imagePath", food.getFoodImage());
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa món ăn này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        foodCRUD.deleteFood(food.getFoodId());
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private String getCategoryNameById(int categoryId) {
        if (categoryId == 1) return "Món miền Bắc";
        else if (categoryId == 2) return "Món miền Trung";
        else if (categoryId == 3) return "Món miền Nam";
        else return "Khác";
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
