package com.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        TextView tvRecipeName = findViewById(R.id.tvRecipeName);
        TextView tvRecipeDescription =
                findViewById(R.id.tvRecipeDescription);
        TextView tvRecipeIngredients =
                findViewById(R.id.tvRecipeIngredients);

        String name = getIntent().getStringExtra("recipe_name");
        String description =
                getIntent().getStringExtra("recipe_description");

        tvRecipeName.setText(
                name == null ? "Recipe Details" : name
        );

        tvRecipeDescription.setText(
                description == null ? "" : description
        );

        int ingredientCount = getIntent().getIntExtra(
                "ingredient_count", 0
        );

        StringBuilder text = new StringBuilder();
        text.append("Ingredients required:\n\n");

        if (ingredientCount > 0) {
            for (int i = 0; i < ingredientCount; i++) {

                String ingredient = getIntent().getStringExtra(
                        "ingredient_name_" + i
                );

                double quantity = getIntent().getDoubleExtra(
                        "ingredient_quantity_" + i, 0.0
                );

                text.append("- ")
                        .append(ingredient == null
                                ? "Unknown ingredient" : ingredient)
                        .append(": ")
                        .append(String.format(
                                Locale.getDefault(), "%s", quantity
                        ))
                        .append("\n");
            }
        } else {
            text.append("No ingredient information was received.");
        }

        tvRecipeIngredients.setText(text.toString());
    }
}
