
package com.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_INGREDIENTS = "ingredients";

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY = "expiry_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_INGREDIENTS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_QUANTITY + " REAL NOT NULL, "
                + COL_UNIT + " TEXT NOT NULL, "
                + COL_EXPIRY + " TEXT"
                + ")";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        onCreate(db);
    }

    public boolean addIngredient(String name, double quantity,
                                 String unit, String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit);
        values.put(COL_EXPIRY, expiryDate);

        long result = db.insert(TABLE_INGREDIENTS, null, values);

        return result != -1;
    }

    public Cursor getAllIngredients() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                TABLE_INGREDIENTS,
                null,
                null,
                null,
                null,
                null,
                COL_NAME + " ASC"
        );
    }

    public boolean deleteIngredient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(
                TABLE_INGREDIENTS,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return rowsDeleted > 0;
    }

    public boolean updateIngredient(
            int id,
            String name,
            double quantity,
            String unit,
            String expiryDate
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit);
        values.put(COL_EXPIRY, expiryDate);

        int rowsUpdated = db.update(
                TABLE_INGREDIENTS,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return rowsUpdated > 0;
    }

}
