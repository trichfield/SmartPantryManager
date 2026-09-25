package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pantry.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "pantry_items";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY = "expiry";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_QUANTITY + " TEXT, " +
                COLUMN_UNIT + " TEXT, " +
                COLUMN_EXPIRY + " TEXT)";
        db.execSQL(createTable);

        String createRecipes = "CREATE TABLE Recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, ingredients TEXT, instructions TEXT)";
        db.execSQL(createRecipes);

        db.execSQL("INSERT INTO Recipes (name, ingredients, instructions) VALUES ('Tomato Pasta', 'pasta:200g, tomato:2, garlic:1', 'Boil pasta, make sauce')");
        db.execSQL("INSERT INTO Recipes (name, ingredients, instructions) VALUES ('Egg Fried Rice', 'rice:1 cup, egg:2, oil:1 tbsp', 'Fry eggs and mix with rice')");
        db.execSQL("INSERT INTO Recipes (name, ingredients, instructions) VALUES ('Potato Mash', 'potato:3, butter:20g, milk:50ml', 'Boil and mash')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS Recipes");
        onCreate(db);
    }

    public long addItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_UNIT, item.getUnit());
        values.put(COLUMN_EXPIRY, item.getExpiryDate());
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public List<PantryItem> getAllItems() {
        List<PantryItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String qty = cursor.getString(2);
                String unit = cursor.getString(3);
                String expiry = cursor.getString(4);
                itemList.add(new PantryItem(id, name, qty, unit, expiry));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }

    public List<PantryItem> getAllPantryItems() {
        return getAllItems();
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Recipes", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Recipe(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void addPantryItem(String name, double qty, String unit, String expiry) {
        addItem(new PantryItem(0, name, String.valueOf(qty), unit, expiry));
    }

    public void updatePantryItem(int id, String name, double qty, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, String.valueOf(qty));
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_EXPIRY, expiry);
        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}