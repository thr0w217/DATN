package com.example.loginsignup.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.loginsignup.DB.Food;
import com.example.loginsignup.DB.FoodAdapter;
import com.example.loginsignup.DB.FoodCRUD;
import com.example.loginsignup.R;
import android.content.Context;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private FoodCRUD foodCRUD;
    private List<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodCRUD = new FoodCRUD(this);

        recyclerView = findViewById(R.id.recyclerViewFood);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        loadFoodData();

        ImageButton centerButton = findViewById(R.id.center_button);
        centerButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_add) {
                    Intent addIntent = new Intent(MainActivity.this, AddFoodActivity.class);
                    startActivity(addIntent);
                } else if (id == R.id.action_edit) {
                    Intent editIntent = new Intent(MainActivity.this, EditFoodActivity.class);
                    startActivity(editIntent);
                } else if (id == R.id.action_delete) {
                    Intent deleteIntent = new Intent(MainActivity.this, DeleteFoodActivity.class);
                    startActivity(deleteIntent);
                }
                return true;
            });
            popupMenu.show();
        });
        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.setFocusable(false);
        etSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }

    private void loadFoodData() {
        List<Food> allFoods = foodCRUD.getAllFoods();

        //Yêu thích
        SharedPreferences prefs = getSharedPreferences("favorites", Context.MODE_PRIVATE);
        Collections.sort(allFoods, (f1, f2) -> {
            boolean fav1 = prefs.getBoolean("food_" + f1.getFoodId(), false);
            boolean fav2 = prefs.getBoolean("food_" + f2.getFoodId(), false);
            return Boolean.compare(fav2, fav1);
        });

        foodList = allFoods;
        adapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadFoodData();
    }
}
