package com.example.loginsignup.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.loginsignup.DB.FoodCRUD;
import com.example.loginsignup.R;

public class DeleteFoodActivity extends AppCompatActivity {

    private TextView tvConfirm;
    private Button btnDelete, btnCancel;
    private FoodCRUD foodCRUD;
    private int currentFoodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_food);


        tvConfirm = findViewById(R.id.tvConfirm);
        btnDelete = findViewById(R.id.btnDelete);
        btnCancel = findViewById(R.id.btnCancel);


        currentFoodId = getIntent().getIntExtra("foodId", -1);


        foodCRUD = new FoodCRUD(this);


        tvConfirm.setText("Bạn có chắc muốn xóa món ăn này?");


        btnDelete.setOnClickListener(v -> {

            foodCRUD.deleteFoodDetail(currentFoodId);

            int rowsAffected = foodCRUD.deleteFood(currentFoodId);
            if (rowsAffected > 0) {
                finish();
            }
        });


        btnCancel.setOnClickListener(v -> finish());
    }
}
