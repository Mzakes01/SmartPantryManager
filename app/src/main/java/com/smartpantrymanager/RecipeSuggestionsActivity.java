
package com.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecipeSuggestionsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView recipeListView;
    private TextView tvRecipeMessage;

    private final ArrayList<Recipe> suggestedRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_suggestions);

        databaseHelper = new DatabaseHelper(this);
        recipeListView = findViewById(R.id.recipeListView);
        tvRecipeMessage = findViewById(R.id.tvRecipeMessage);

        RecipeAdapter adapter = new RecipeAdapter(this, suggestedRecipes);
        recipeListView.setAdapter(adapter);

        loadSuggestions(adapter);

        recipeListView.setOnItemClickListener((parent, view, position, id) -> {
            Recipe recipe = suggestedRecipes.get(position);

            Intent intent = new Intent(
                    RecipeSuggestionsActivity.this,
                    RecipeDetailsActivity.class
            );

            intent.putExtra("recipe_name", recipe.getName());
            intent.putExtra("recipe_description", recipe.getDescription());

            Map<String, Double> ingredients =
                    recipe.getRequiredIngredients();

            intent.putExtra("ingredient_count", ingredients.size());

            int index = 0;

            for (Map.Entry<String, Double> entry : ingredients.entrySet()) {
                intent.putExtra(
                        "ingredient_name_" + index,
                        entry.getKey()
                );

                intent.putExtra(
                        "ingredient_quantity_" + index,
                        entry.getValue()
                );

                index++;
            }

            startActivity(intent);
        });
    }

    private void loadSuggestions(RecipeAdapter adapter) {

        suggestedRecipes.clear();

        Map<String, Double> pantry = new HashMap<>();

        try (Cursor cursor = databaseHelper.getAllIngredients()) {

            while (cursor.moveToNext()) {

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)
                ).trim().toLowerCase(Locale.ROOT);

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY)
                );

                pantry.merge(name, quantity, Double::sum);
            }

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Could not read pantry ingredients",
                    Toast.LENGTH_SHORT
            ).show();

            tvRecipeMessage.setText("Unable to load recipes. Please try again.");
            return;
        }

        List<Recipe> allRecipes = RecipeRepository.getRecipes();

        for (Recipe recipe : allRecipes) {
            if (recipe.canPrepare(pantry)) {
                suggestedRecipes.add(recipe);
            }
        }

        adapter.notifyDataSetChanged();

        if (suggestedRecipes.isEmpty()) {
            tvRecipeMessage.setText(
                    "No recipes match your pantry yet. Add more ingredients!"
            );
        } else {
            tvRecipeMessage.setText(
                    "You can prepare " + suggestedRecipes.size() + " recipe(s)!"
            );
        }
    }
}
