package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COL_PANTRY_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_QTY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY = "expiry_date";

    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "id";
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_INGREDIENTS = "ingredients";
    public static final String COL_INSTRUCTIONS = "instructions";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantry = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_QTY + " REAL NOT NULL, " +
                COL_UNIT + " TEXT NOT NULL, " +
                COL_EXPIRY + " TEXT)";

        String createRecipes = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT NOT NULL, " +
                COL_INGREDIENTS + " TEXT NOT NULL, " +
                COL_INSTRUCTIONS + " TEXT NOT NULL)";

        db.execSQL(createPantry);
        db.execSQL(createRecipes);

        insertRecipe(db, "Scrambled Eggs", "Eggs, Milk, Salt, Butter", "Beat eggs with milk. Melt butter in pan. Cook eggs on low heat.");
        insertRecipe(db, "Grilled Cheese", "Bread, Cheese, Butter", "Butter bread. Place cheese between bread. Grill until golden.");
        insertRecipe(db, "Pasta with Tomato Sauce", "Pasta, Tomato Sauce, Salt, Oil", "Boil pasta. Heat sauce. Mix together.");
        insertRecipe(db, "Rice and Beans", "Rice, Beans, Salt, Water", "Cook rice. Heat beans. Serve together.");
        insertRecipe(db, "Omelette", "Eggs, Cheese, Onion, Salt", "Beat eggs. Chop onion. Cook with cheese inside.");
        insertRecipe(db, "Chicken Stir Fry", "Chicken, Vegetables, Soy Sauce, Oil", "Cut chicken. Fry with vegetables and sauce.");
        insertRecipe(db, "Vegetable Soup", "Carrots, Potatoes, Onion, Water, Salt", "Chop vegetables. Boil in water with salt until soft.");
        insertRecipe(db, "Peanut Butter Sandwich", "Bread, Peanut Butter, Jam", "Spread peanut butter and jam on bread.");
        insertRecipe(db, "Boiled Eggs", "Eggs, Water, Salt", "Boil water. Add eggs for 10 minutes. Peel.");
        insertRecipe(db, "Fried Rice", "Rice, Eggs, Vegetables, Soy Sauce", "Fry leftover rice with eggs and vegetables and sauce.");
        insertRecipe(db, "Spaghetti Bolognese", "Spaghetti, Mince, Tomato Sauce, Onion", "Cook spaghetti. Fry mince and onion. Add sauce.");
        insertRecipe(db, "Chicken Salad", "Chicken, Lettuce, Tomato, Cucumber", "Cook chicken. Chop vegetables. Mix all together.");
        insertRecipe(db, "Tuna Sandwich", "Bread, Tuna, Mayonnaise, Lettuce", "Mix tuna with mayo. Make sandwich with lettuce.");
        insertRecipe(db, "Beef Stew", "Beef, Potatoes, Carrots, Onion, Water", "Brown beef. Add chopped veg and water. Simmer 1 hour.");
        insertRecipe(db, "Pancakes", "Flour, Milk, Eggs, Sugar, Oil", "Mix flour milk eggs sugar. Fry small circles in oil.");
        insertRecipe(db, "French Toast", "Bread, Eggs, Milk, Sugar, Butter", "Dip bread in egg milk mix. Fry in butter.");
        insertRecipe(db, "Garlic Bread", "Bread, Butter, Garlic, Parsley", "Mix butter garlic parsley. Spread on bread. Bake.");
        insertRecipe(db, "Mashed Potatoes", "Potatoes, Butter, Milk, Salt", "Boil potatoes until soft. Mash with butter milk salt.");
        insertRecipe(db, "Chicken Curry", "Chicken, Curry Powder, Onion, Rice", "Fry onion. Add chicken and curry. Serve with rice.");
        insertRecipe(db, "Vegetable Fried Noodles", "Noodles, Vegetables, Soy Sauce, Oil", "Cook noodles. Stir fry vegetables. Mix with sauce.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    private void insertRecipe(SQLiteDatabase db, String name, String ingredients, String instructions) {
        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_NAME, name);
        values.put(COL_INGREDIENTS, ingredients);
        values.put(COL_INSTRUCTIONS, instructions);
        db.insert(TABLE_RECIPES, null, values);
    }

    public long addPantryItem(String name, double qty, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name.toLowerCase().trim());
        cv.put(COL_QTY, qty);
        cv.put(COL_UNIT, unit);
        cv.put(COL_EXPIRY, expiry);
        return db.insert(TABLE_PANTRY, null, cv);
    }

    public Cursor getAllPantryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PANTRY, null);
    }

    public int updatePantryItem(int id, String name, double qty, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name.toLowerCase().trim());
        cv.put(COL_QTY, qty);
        cv.put(COL_UNIT, unit);
        cv.put(COL_EXPIRY, expiry);
        return db.update(TABLE_PANTRY, cv, COL_PANTRY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PANTRY, COL_PANTRY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Cursor getAllRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);
    }
}