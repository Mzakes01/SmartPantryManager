package com.smartpantrymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class IngredientAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> ingredients;

    public IngredientAdapter(Context context, List<String> ingredients) {
        super(context, 0, ingredients);
        this.context = context;
        this.ingredients = ingredients;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    android.R.layout.simple_list_item_2,
                    parent,
                    false
            );
        }

        TextView title = view.findViewById(android.R.id.text1);
        TextView subtitle = view.findViewById(android.R.id.text2);

        String ingredient = ingredients.get(position);
        String[] parts = ingredient.split("\nExpires: ", 2);
        String[] details = parts[0].split(" — ", 2);

        title.setText(details[0]);

        if (details.length > 1) {
            subtitle.setText(details[1]);
        } else {
            subtitle.setText("");
        }

        if (parts.length > 1) {
            subtitle.append("\nExpires: " + parts[1]);
        }

        return view;
    }
}
