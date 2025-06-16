package com.example.loginsignup.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.loginsignup.DB.Food;
import com.example.loginsignup.DB.FoodAdapter;
import com.example.loginsignup.DB.FoodCRUD;
import com.example.loginsignup.DB.FoodDetail;
import com.example.loginsignup.R;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText etSearch;
    private RecyclerView rvHistory;
    private FoodAdapter adapter;
    private FoodCRUD foodCRUD;
    private List<Food> fullFoodList;
    private List<Food> filteredFoodList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Ánh xạ Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập nút back cho Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Xử lý sự kiện click nút back
        toolbar.setNavigationOnClickListener(v -> finish());

        // Ánh xạ các view khác
        etSearch = findViewById(R.id.etSearch);
        rvHistory = findViewById(R.id.rvHistory);

        // Khởi tạo
        foodCRUD = new FoodCRUD(this);
        fullFoodList = foodCRUD.getAllFoods();
        filteredFoodList = new ArrayList<>(fullFoodList);

        // Thiết lập RecyclerView
        adapter = new FoodAdapter(filteredFoodList);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(adapter);

        // Focus và hiển thị bàn phím khi vào màn hình
        etSearch.requestFocus();

        // Xử lý sự kiện tìm kiếm
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFoodList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterFoodList(String query) {
        query = query.toLowerCase().trim();
        filteredFoodList.clear();
        if (query.isEmpty()) {
            filteredFoodList.addAll(fullFoodList);
        } else {
            for (Food food : fullFoodList) {
                FoodDetail foodDetail = foodCRUD.getFoodDetailInfo(food.getFoodId());
                String material = (foodDetail != null) ? foodDetail.getMaterial() : "";

                int categoryId = food.getCategoryId();

                String categoryName = getCategoryNameById(categoryId);

                if (food.getFoodName().toLowerCase().contains(query) ||
                        material.toLowerCase().contains(query) ||
                        categoryName.toLowerCase().contains(query)) {
                    filteredFoodList.add(food);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private String getCategoryNameById(int categoryId) {
        if (categoryId == 1) return "Món miền Bắc";
        else if (categoryId == 2) return "Món miền Trung";
        else if (categoryId == 3) return "Món miền Nam";
        else return "Khác";
    }
}
