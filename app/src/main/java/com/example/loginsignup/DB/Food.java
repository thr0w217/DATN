package com.example.loginsignup.DB;

import android.database.Cursor;
import java.io.Serializable;

public class Food implements Serializable {
    private int foodId;
    private String foodName;
    private String foodImage;
    private int categoryId;

    private boolean isFavorite;

    public Food(int foodId, String foodName, String foodImage, int categoryId) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.categoryId = categoryId;
    }

    public Food(Cursor cursor) {
        this.foodId = cursor.getInt(cursor.getColumnIndexOrThrow(DBConnect.COLUMN_FOOD_ID));
        this.foodName = cursor.getString(cursor.getColumnIndexOrThrow(DBConnect.COLUMN_FOOD_NAME));
        this.foodImage = cursor.getString(cursor.getColumnIndexOrThrow(DBConnect.COLUMN_FOOD_IMAGE));
        this.categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DBConnect.COLUMN_FOOD_CATEGORY_ID));
    }

    public int getFoodId() { return foodId; }
    public String getFoodName() { return foodName; }
    public String getFoodImage() { return foodImage; }
    public int getCategoryId() { return categoryId; }

    public void setFoodId(int foodId) { this.foodId = foodId; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public void setFoodImage(String foodImage) { this.foodImage = foodImage; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
}
