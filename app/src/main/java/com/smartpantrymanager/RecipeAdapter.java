
package com.smartpantrymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        super(context, 0, recipes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Recipe recipe = getItem(position);

        TextView recipeName = convertView.findViewById(android.R.id.text1);
        TextView recipeDescription = convertView.findViewById(android.R.id.text2);

        if (recipe != null) {
            recipeName.setText(recipe.getName());
            recipeDescription.setText(recipe.getDescription());
        }

        return convertView;
    }
}
