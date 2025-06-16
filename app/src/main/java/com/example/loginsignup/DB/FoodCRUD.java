package com.example.loginsignup.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class FoodCRUD {
    private DBConnect dbHelper;

    public FoodCRUD(Context context) {
        dbHelper = new DBConnect(context);
    }

    // ========== FOOD TABLE ==========

    public List<Food> getAllFoods() {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBConnect.TABLE_FOOD, null);
        if (cursor.moveToFirst()) {
            do {
                Food food = new Food(cursor);
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return foodList;
    }

    public long addFood(String name, String image, int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConnect.COLUMN_FOOD_NAME, name);
        values.put(DBConnect.COLUMN_FOOD_IMAGE, image);
        values.put(DBConnect.COLUMN_FOOD_CATEGORY_ID, categoryId);
        long result = db.insert(DBConnect.TABLE_FOOD, null, values);
        return result;
    }

    public int updateFood(int foodId, String name, String image, int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConnect.COLUMN_FOOD_NAME, name);
        values.put(DBConnect.COLUMN_FOOD_IMAGE, image);
        values.put(DBConnect.COLUMN_FOOD_CATEGORY_ID, categoryId);
        int rowsAffected = db.update(
                DBConnect.TABLE_FOOD,
                values,
                DBConnect.COLUMN_FOOD_ID + "=?",
                new String[]{String.valueOf(foodId)}
        );
        return rowsAffected;
    }

    public int deleteFood(int foodId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete(
                DBConnect.TABLE_FOOD,
                DBConnect.COLUMN_FOOD_ID + "=?",
                new String[]{String.valueOf(foodId)}
        );
        return rowsAffected;
    }

    // ========== FOOD_DETAIL TABLE ==========

    public long addFoodDetail(int foodId, String description, String material, String recipe) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConnect.COLUMN_FOOD_DETAIL_FOOD_ID, foodId);
        values.put(DBConnect.COLUMN_FOOD_DESCRIPTION, description);
        values.put(DBConnect.COLUMN_FOOD_MATERIAL, material);
        values.put(DBConnect.COLUMN_FOOD_RECIPE, recipe);
        long result = db.insert(DBConnect.TABLE_FOOD_DETAIL, null, values);
        return result;
    }

    public FoodDetail getFoodDetailInfo(int foodId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DBConnect.TABLE_FOOD_DETAIL,
                null,
                DBConnect.COLUMN_FOOD_DETAIL_FOOD_ID + "=?",
                new String[]{String.valueOf(foodId)},
                null, null, null
        );
        FoodDetail detail = null;
        if (cursor.moveToFirst()) {
            detail = new FoodDetail(cursor);
        }
        cursor.close();
        return detail;
    }

    public int updateFoodDetail(int foodId, String description, String recipe) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConnect.COLUMN_FOOD_DESCRIPTION, description);
        values.put(DBConnect.COLUMN_FOOD_RECIPE, recipe);
        int rowsAffected = db.update(
                DBConnect.TABLE_FOOD_DETAIL,
                values,
                DBConnect.COLUMN_FOOD_DETAIL_FOOD_ID + "=?",
                new String[]{String.valueOf(foodId)}
        );
        return rowsAffected;
    }

    public int deleteFoodDetail(int foodId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete(
                DBConnect.TABLE_FOOD_DETAIL,
                DBConnect.COLUMN_FOOD_DETAIL_FOOD_ID + "=?",
                new String[]{String.valueOf(foodId)}
        );
        return rowsAffected;
    }
}
