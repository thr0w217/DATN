package com.example.loginsignup.DB;

import android.database.Cursor;
import java.io.Serializable;

public class FoodDetail implements Serializable {
    private int foodId;
    private String description;
    private String recipe;
    private String material;

    public FoodDetail(int foodId, String description, String recipe, String material) {
        this.foodId = foodId;
        this.description = description;
        this.recipe = recipe;
        this.material = material;
    }

    public FoodDetail(Cursor cursor) {
        this.foodId = cursor.getInt(cursor.getColumnIndexOrThrow(DBConnect.COLUMN_FOOD_DETAIL_FOOD_ID));

        int descIdx = cursor.getColumnIndex(DBConnect.COLUMN_FOOD_DESCRIPTION);
        if (descIdx >= 0) {
            this.description = cursor.getString(descIdx);
        } else {
            this.description = "";
        }

        int recipeIdx = cursor.getColumnIndex(DBConnect.COLUMN_FOOD_RECIPE);
        if (recipeIdx >= 0) {
            this.recipe = cursor.getString(recipeIdx);
        } else {
            this.recipe = "";
        }

        int matIdx = cursor.getColumnIndex(DBConnect.COLUMN_FOOD_MATERIAL);
        if (matIdx >= 0) {
            this.material = cursor.getString(matIdx);
        } else {
            this.material = "";
        }
    }



    public int getFoodId() { return foodId; }
    public String getDescription() { return description; }
    public String getRecipe() { return recipe; }
    public String getMaterial() {return material; }

    public void setFoodId(int foodId) { this.foodId = foodId; }
    public void setDescription(String description) { this.description = description; }
    public void setRecipe(String recipe) { this.recipe = recipe; }
    public void setMaterial(String material) { this.material = material; }
}
