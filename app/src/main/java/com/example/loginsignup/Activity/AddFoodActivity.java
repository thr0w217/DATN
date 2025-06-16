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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.loginsignup.DB.FoodCRUD;
import com.example.loginsignup.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddFoodActivity extends AppCompatActivity {

    private EditText etFoodName, etDescription, etMaterial, etRecipe;
    private Spinner spCategory;
    private Button btnAddFood;
    private FoodCRUD foodCRUD;
    private ImageView ivFoodImage;
    private Button btnPickImage;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        ivFoodImage = findViewById(R.id.ivFoodImage);
        btnPickImage = findViewById(R.id.btnPickImage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        etFoodName = findViewById(R.id.etFoodName);
        etDescription = findViewById(R.id.etDescription);
        etMaterial = findViewById(R.id.etMaterial);
        etRecipe = findViewById(R.id.etRecipe);
        spCategory = findViewById(R.id.spCategory);
        btnAddFood = findViewById(R.id.btnAddFood);

        foodCRUD = new FoodCRUD(this);

        setupSpinner();

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            ivFoodImage.setImageURI(selectedImageUri);
                        }
                    }
                }
        );

        btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        btnAddFood.setOnClickListener(v -> {
            String name = etFoodName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String material = etMaterial.getText().toString().trim();
            String recipe = etRecipe.getText().toString().trim();
            int categoryId = spCategory.getSelectedItemPosition() + 1;

            String imagePath = "default_image.jpg"; // Ảnh mặc định

            if (selectedImageUri != null) {
                String savedPath = saveImageToInternalStorage(selectedImageUri);
                if (savedPath != null) {
                    imagePath = savedPath;
                }
            }

            long foodId = foodCRUD.addFood(name, imagePath, categoryId);
            if (foodId != -1) {
                foodCRUD.addFoodDetail((int) foodId, description, material, recipe);
                finish();
            }
        });
    }

    private void setupSpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("Món miền Bắc");
        categories.add("Món miền Trung");
        categories.add("Món miền Nam");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    // Phương thức lưu ảnh vào internal storage
    private String saveImageToInternalStorage(Uri sourceUri) {
        try {
            File storageDir = new File(getFilesDir(), "food_images");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

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
}
