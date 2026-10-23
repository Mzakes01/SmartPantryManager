
package com.smartpantrymanager;

import java.util.HashMap;
import java.util.Map;

public class Recipe {

    private final String name;
    private final String description;
    private final Map<String, Double> requiredIngredients;

    public Recipe(String name, String description,
                  Map<String, Double> requiredIngredients) {
        this.name = name;
        this.description = description;
        this.requiredIngredients = requiredIngredients;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Double> getRequiredIngredients() {
        return requiredIngredients;
    }

    public boolean canPrepare(Map<String, Double> pantry) {
        for (Map.Entry<String, Double> ingredient
                : requiredIngredients.entrySet()) {

            String requiredName = ingredient.getKey().trim().toLowerCase();
            double requiredQuantity = ingredient.getValue();

            Double availableQuantity = pantry.get(requiredName);

            if (availableQuantity == null
                    || availableQuantity < requiredQuantity) {
                return false;
            }
        }

        return true;
    }
}
