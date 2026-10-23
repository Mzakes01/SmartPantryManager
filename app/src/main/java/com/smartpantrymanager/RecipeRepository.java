
package com.smartpantrymanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeRepository {

    private static Map<String, Double> ingredients(Object... items) {
        Map<String, Double> result = new HashMap<>();

        for (int i = 0; i < items.length; i += 2) {
            result.put(
                    items[i].toString().toLowerCase(),
                    ((Number) items[i + 1]).doubleValue()
            );
        }

        return result;
    }

    public static List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        recipes.add(new Recipe("Plain Rice",
                "A simple serving of cooked rice.",
                ingredients("rice", 1.0)));

        recipes.add(new Recipe("Rice and Beans",
                "A filling combination of rice and beans.",
                ingredients("rice", 1.0, "beans", 1.0)));

        recipes.add(new Recipe("Tomato Pasta",
                "Pasta cooked with tomatoes.",
                ingredients("pasta", 1.0, "tomato", 2.0)));

        recipes.add(new Recipe("Egg Omelette",
                "A simple omelette made with eggs.",
                ingredients("eggs", 2.0)));

        recipes.add(new Recipe("Tomato Omelette",
                "An omelette with fresh tomatoes.",
                ingredients("eggs", 2.0, "tomato", 1.0)));

        recipes.add(new Recipe("Mashed Potatoes",
                "Soft mashed potatoes.",
                ingredients("potatoes", 3.0, "milk", 0.25)));

        recipes.add(new Recipe("Boiled Potatoes",
                "Simple boiled potatoes.",
                ingredients("potatoes", 3.0)));

        recipes.add(new Recipe("Fried Eggs",
                "Eggs cooked in oil.",
                ingredients("eggs", 2.0, "oil", 0.02)));

        recipes.add(new Recipe("Banana Smoothie",
                "A blended banana and milk drink.",
                ingredients("banana", 1.0, "milk", 0.25)));

        recipes.add(new Recipe("Peanut Butter Sandwich",
                "Bread with peanut butter.",
                ingredients("bread", 2.0, "peanut butter", 0.03)));

        recipes.add(new Recipe("Cheese Sandwich",
                "A simple cheese sandwich.",
                ingredients("bread", 2.0, "cheese", 2.0)));

        recipes.add(new Recipe("Garlic Pasta",
                "Pasta flavoured with garlic and oil.",
                ingredients("pasta", 1.0, "garlic", 1.0, "oil", 0.02)));

        recipes.add(new Recipe("Vegetable Rice",
                "Rice mixed with carrots and peas.",
                ingredients("rice", 1.0, "carrots", 1.0, "peas", 1.0)));

        recipes.add(new Recipe("Carrot Soup",
                "A basic carrot soup.",
                ingredients("carrots", 3.0, "onion", 1.0)));

        recipes.add(new Recipe("Tomato Soup",
                "A simple tomato soup.",
                ingredients("tomato", 3.0, "onion", 1.0)));

        recipes.add(new Recipe("Banana Pancakes",
                "Pancakes made with banana and eggs.",
                ingredients("banana", 1.0, "eggs", 1.0, "flour", 0.5)));

        recipes.add(new Recipe("French Toast",
                "Bread dipped in egg and cooked.",
                ingredients("bread", 2.0, "eggs", 1.0, "milk", 0.1)));

        recipes.add(new Recipe("Chicken and Rice",
                "Chicken served with rice.",
                ingredients("chicken", 1.0, "rice", 1.0)));

        recipes.add(new Recipe("Baked Potatoes",
                "Oven-baked potatoes.",
                ingredients("potatoes", 3.0, "oil", 0.02)));

        recipes.add(new Recipe("Apple Smoothie",
                "A blended apple and milk drink.",
                ingredients("apple", 1.0, "milk", 0.25)));

        return recipes;
    }
}
