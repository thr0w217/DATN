package com.example.loginsignup.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnect extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app_database.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CATEGORY = "Category";
    public static final String TABLE_ADMIN = "Admin";
    public static final String TABLE_USER = "User";
    public static final String TABLE_FOOD = "Food";
    public static final String TABLE_FOOD_DETAIL = "Food_detail";


    public static final String COLUMN_ID = "id";

    //Category
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";

    //Admin
    public static final String COLUMN_ADMIN_ID = "admin_id";
    public static final String COLUMN_ADMIN_NAME = "admin_name";
    public static final String COLUMN_ADMIN_EMAIL = "email";

    //User
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_EMAIL = "email";

    //Food
    public static final String COLUMN_FOOD_ID = "food_id";
    public static final String COLUMN_FOOD_NAME = "food_name";
    public static final String COLUMN_FOOD_IMAGE = "food_image";
    public static final String COLUMN_FOOD_CATEGORY_ID = "category_id";

    //Food_detail
    public static final String COLUMN_FOOD_DETAIL_ID = "food_detail_id";
    public static final String COLUMN_FOOD_DETAIL_FOOD_ID = "food_id";
    public static final String COLUMN_FOOD_DESCRIPTION = "description";
    public static final String COLUMN_FOOD_MATERIAL = "material";
    public static final String COLUMN_FOOD_RECIPE = "recipe";

    public DBConnect(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + " (" +
                COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT NOT NULL" +
                ");";


        String CREATE_ADMIN_TABLE = "CREATE TABLE " + TABLE_ADMIN + " (" +
                COLUMN_ADMIN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_ADMIN_NAME + " TEXT, " +
                COLUMN_ADMIN_EMAIL + " TEXT" +
                ");";


        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_USER_ID + " TEXT PRIMARY KEY, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_EMAIL + " TEXT" +
                ");";


        String CREATE_FOOD_TABLE = "CREATE TABLE " + TABLE_FOOD + " (" +
                COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FOOD_NAME + " TEXT NOT NULL, " +
                COLUMN_FOOD_IMAGE + " TEXT, " +
                COLUMN_FOOD_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_FOOD_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + ")" +
                ");";


        String CREATE_FOOD_DETAIL_TABLE = "CREATE TABLE " + TABLE_FOOD_DETAIL + " (" +
                COLUMN_FOOD_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FOOD_DETAIL_FOOD_ID + " INTEGER, " +
                COLUMN_FOOD_DESCRIPTION + " TEXT, " +
                COLUMN_FOOD_RECIPE + " TEXT, " +
                COLUMN_FOOD_MATERIAL + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_FOOD_DETAIL_FOOD_ID + ") REFERENCES " + TABLE_FOOD + "(" + COLUMN_FOOD_ID + ")" +
                ");";

        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_ADMIN_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_FOOD_TABLE);
        db.execSQL(CREATE_FOOD_DETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }
}
