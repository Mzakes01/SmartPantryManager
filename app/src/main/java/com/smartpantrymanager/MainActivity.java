
package com.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView pantryListView;
    private ArrayAdapter<String> adapter;
    private final ArrayList<String> ingredientList = new ArrayList<>();
    private final ArrayList<Integer> ingredientIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        pantryListView = findViewById(R.id.pantryListView);
        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);
        Button btnRecipeSuggestions = findViewById(R.id.btnRecipeSuggestions);
        Button btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );
            startActivity(intent);
        });

        adapter = new IngredientAdapter(this, ingredientList);

        pantryListView.setAdapter(adapter);

        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddIngredientActivity.class
            );
            startActivity(intent);
        });

        btnRecipeSuggestions.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    RecipeSuggestionsActivity.class
            );
            startActivity(intent);
        });


        pantryListView.setOnItemClickListener((parent, view, position, id) -> {
            int ingredientId = ingredientIds.get(position);
            String ingredient = ingredientList.get(position);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Manage Ingredient")
                    .setMessage("What would you like to do with " + ingredient + "?")
                    .setPositiveButton("Edit", (dialog, which) -> {

                        try (Cursor cursor = databaseHelper.getReadableDatabase().query(
                                DatabaseHelper.TABLE_INGREDIENTS,
                                null,
                                DatabaseHelper.COL_ID + " = ?",
                                new String[]{String.valueOf(ingredientId)},
                                null, null, null)) {

                            if (cursor.moveToFirst()) {
                                Intent intent = new Intent(
                                        MainActivity.this,
                                        EditIngredientActivity.class
                                );

                                intent.putExtra("ingredient_id", ingredientId);
                                intent.putExtra("ingredient_name",
                                        cursor.getString(cursor.getColumnIndexOrThrow(
                                                DatabaseHelper.COL_NAME)));
                                intent.putExtra("ingredient_quantity",
                                        String.valueOf(cursor.getDouble(
                                                cursor.getColumnIndexOrThrow(
                                                        DatabaseHelper.COL_QUANTITY))));
                                intent.putExtra("ingredient_unit",
                                        cursor.getString(cursor.getColumnIndexOrThrow(
                                                DatabaseHelper.COL_UNIT)));
                                intent.putExtra("ingredient_expiry",
                                        cursor.getString(cursor.getColumnIndexOrThrow(
                                                DatabaseHelper.COL_EXPIRY)));

                                startActivity(intent);
                            }
                        }
                    })
                    .setNegativeButton("Delete", (dialog, which) -> {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Confirm Delete")
                                .setMessage("Delete this ingredient?")
                                .setPositiveButton("Yes", (d, w) -> {
                                    if (databaseHelper.deleteIngredient(ingredientId)) {
                                        Toast.makeText(this, "Ingredient deleted",
                                                Toast.LENGTH_SHORT).show();
                                        loadIngredients();
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    })
                    .setNeutralButton("Cancel", null)
                    .show();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadIngredients();
    }

    private void loadIngredients() {
        ingredientList.clear();
        ingredientIds.clear();

        try (Cursor cursor = databaseHelper.getAllIngredients()) {
            while (cursor.moveToNext()) {
                int ingredientId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY));

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UNIT));

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EXPIRY));

                String item = name + " — " + quantity + " " + unit;

                if (expiryDate != null && !expiryDate.isEmpty()) {
                    item += "\nExpires: " + expiryDate;
                }

                ingredientIds.add(ingredientId);
                ingredientList.add(item);
            }
        } catch (Exception e) {
            Toast.makeText(this,
                    "Could not load ingredients",
                    Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }
}
