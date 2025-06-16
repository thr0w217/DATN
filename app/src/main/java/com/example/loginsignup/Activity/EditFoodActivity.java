package com.example.loginsignup.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.loginsignup.DB.FoodCRUD;
import com.example.loginsignup.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditFoodActivity extends AppCompatActivity {

    private EditText etFoodName, etDescription, etRecipe;
    private Spinner spCategory;
    private ImageView ivFoodImage;
    private Button btnUpdate, btnPickImage;
    private FoodCRUD foodCRUD;
    private int currentFoodId;
    private String currentImagePath = "";
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        etFoodName = findViewById(R.id.etFoodName);
        etDescription = findViewById(R.id.etDescription);
        etRecipe = findViewById(R.id.etRecipe);
        spCategory = findViewById(R.id.spCategory);
        ivFoodImage = findViewById(R.id.ivFoodImage);
        btnPickImage = findViewById(R.id.btnPickImage);
        btnUpdate = findViewById(R.id.btnUpdate);

        foodCRUD = new FoodCRUD(this);

        currentFoodId = getIntent().getIntExtra("foodId", -1);
        if (currentFoodId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy món ăn", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupCategorySpinner();
        loadFoodData();

        btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnUpdate.setOnClickListener(v -> updateFood());
    }

    private void setupCategorySpinner() {
        String[] categories = {"Món miền Bắc", "Món miền Trung", "Món miền Nam", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    private void loadFoodData() {
        String foodName = getIntent().getStringExtra("foodName");
        String description = getIntent().getStringExtra("description");
        String recipe = getIntent().getStringExtra("recipe");
        int categoryId = getIntent().getIntExtra("categoryId", 1);
        String imagePath = getIntent().getStringExtra("imagePath");

        etFoodName.setText(foodName);
        etDescription.setText(description);
        if (etRecipe != null) etRecipe.setText(recipe);
        spCategory.setSelection(categoryId - 1);

        if (imagePath != null && !imagePath.isEmpty()) {
            int resId = getResources().getIdentifier(imagePath, "drawable", getPackageName());
            if (resId != 0) {
                ivFoodImage.setImageResource(resId);
            } else {
                ivFoodImage.setImageResource(R.drawable.placeholder);
            }
            currentImagePath = imagePath;
        } else {
            ivFoodImage.setImageResource(R.drawable.placeholder

            );
        }
    }

    private void updateFood() {
        String name = etFoodName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String recipe = etRecipe.getText().toString().trim();
        int categoryId = spCategory.getSelectedItemPosition() + 1;

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên món ăn", Toast.LENGTH_SHORT).show();
            return;
        }

        int rowsAffected = foodCRUD.updateFood(
                currentFoodId,
                name,
                currentImagePath.isEmpty() ? "default_image.jpg" : currentImagePath,
                categoryId
        );

        if (rowsAffected > 0) {
            foodCRUD.updateFoodDetail(currentFoodId, description, recipe);
            Toast.makeText(this, "Cập nhật món ăn thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    //Lưu ảnh vào internal storage
    private String saveImageToInternalStorage(Uri sourceUri) {
        try {
            File storageDir = new File(getFilesDir(), "food_images");
            if (!storageDir.exists()) storageDir.mkdirs();

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "food_" + timeStamp + ".jpg";
            File destFile = new File(storageDir, fileName);

            try (InputStream in = getContentResolver().openInputStream(sourceUri);
                 OutputStream out = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
            return destFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            ivFoodImage.setImageURI(selectedImage);

            String savedPath = saveImageToInternalStorage(selectedImage);
            if (savedPath != null) {
                currentImagePath = savedPath;
            } else {
                Toast.makeText(this, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
