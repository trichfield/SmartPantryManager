package com.example.smartpantrymanager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 2;
    public DatabaseHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }
    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity REAL, unit TEXT, expiry TEXT)");
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, ingredients TEXT, steps TEXT)");
    }
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS pantry");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        onCreate(db);
    }
    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", item.getName());
        cv.put("quantity", item.getQuantity());
        cv.put("unit", item.getUnit());
        cv.put("expiry", item.getExpiry());
        return db.insert("pantry", null, cv);
    }
    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM pantry", null);
        if (c.moveToFirst()) { do {
            PantryItem i = new PantryItem();
            i.setId(c.getInt(0)); i.setName(c.getString(1)); i.setQuantity(c.getDouble(2)); i.setUnit(c.getString(3)); i.setExpiry(c.getString(4));
            list.add(i);
        } while (c.moveToNext()); }
        c.close(); return list;
    }
    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", item.getName()); cv.put("quantity", item.getQuantity()); cv.put("unit", item.getUnit()); cv.put("expiry", item.getExpiry());
        return db.update("pantry", cv, "id=?", new String[]{String.valueOf(item.getId())});
    }
    public void deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("pantry", "id=?", new String[]{String.valueOf(id)});
    }
    public void clearAllPantry() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("pantry", null, null);
    }
    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor count = db.rawQuery("SELECT COUNT(*) FROM recipes", null);
        count.moveToFirst(); int cnt = count.getInt(0); count.close();
        if (cnt == 0) { seedRecipes(db); }
        Cursor c = db.rawQuery("SELECT * FROM recipes", null);
        if (c.moveToFirst()) { do {
            Recipe r = new Recipe();
            r.setId(c.getInt(0)); r.setName(c.getString(1)); r.setIngredients(c.getString(2)); r.setSteps(c.getString(3));
            list.add(r);
        } while (c.moveToNext()); }
        c.close(); return list;
    }
    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM recipes WHERE id=?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            Recipe r = new Recipe();
            r.setId(c.getInt(0)); r.setName(c.getString(1)); r.setIngredients(c.getString(2)); r.setSteps(c.getString(3));
            c.close(); return r;
        }
        c.close(); return null;
    }
    private void seedRecipes(SQLiteDatabase db) {
        addRecipe(db, "Fried Rice", "rice, eggs, oil, salt", "Cook rice and fry with eggs.");
        addRecipe(db, "Omelette", "eggs, salt, oil", "Beat eggs and fry.");
        addRecipe(db, "Tomato Soup", "tomatoes, salt, water, oil", "Boil tomatoes and blend.");
        addRecipe(db, "Egg Sandwich", "bread, eggs, salt, butter", "Fry egg and put in bread.");
        addRecipe(db, "Pasta", "pasta, tomatoes, salt, oil", "Boil pasta and mix sauce.");
        addRecipe(db, "Rice and Beans", "rice, beans, salt, water", "Boil rice and beans.");
        addRecipe(db, "Garlic Bread", "bread, garlic, butter", "Spread butter and bake.");
        addRecipe(db, "Boiled Eggs", "eggs, water, salt", "Boil eggs.");
        addRecipe(db, "Tomato Rice", "rice, tomatoes, oil, salt", "Fry tomatoes and mix rice.");
        addRecipe(db, "Bean Soup", "beans, water, salt, oil", "Boil beans.");
        addRecipe(db, "Buttered Pasta", "pasta, butter, salt", "Boil pasta and add butter.");
        addRecipe(db, "Cheese Toast", "bread, cheese, butter", "Toast bread with cheese.");
        addRecipe(db, "Onion Rice", "rice, onions, oil, salt", "Fry onions and mix rice.");
        addRecipe(db, "Scrambled Eggs", "eggs, butter, salt", "Scramble eggs.");
        addRecipe(db, "Potato Fry", "potatoes, oil, salt", "Fry potatoes.");
        addRecipe(db, "Milk Tea", "milk, water, sugar, tea", "Boil all together.");
        addRecipe(db, "Veg Sandwich", "bread, tomatoes, onions, salt", "Put veg in bread.");
        addRecipe(db, "Garlic Rice", "rice, garlic, oil, salt", "Fry garlic and mix rice.");
        addRecipe(db, "Bean Toast", "beans, bread, salt", "Heat beans on toast.");
        addRecipe(db, "Simple Salad", "tomatoes, onions, salt, oil", "Chop and mix.");
    }
    private void addRecipe(SQLiteDatabase db, String n, String ing, String st) {
        ContentValues cv = new ContentValues();
        cv.put("name", n); cv.put("ingredients", ing); cv.put("steps", st);
        db.insert("recipes", null, cv);
    }
}