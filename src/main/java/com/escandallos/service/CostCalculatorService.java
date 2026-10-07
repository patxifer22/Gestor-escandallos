package com.escandallos.service;

import com.escandallos.model.Ingredient;
import com.escandallos.model.Recipe;
import com.escandallos.repository.IngredientRepository;
import com.escandallos.repository.RecipeRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de cálculos estadísticos y financieros globales de los escandallos.
 */
public class CostCalculatorService {
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public CostCalculatorService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    public int getTotalIngredientsCount() {
        return ingredientRepository.findAll().size();
    }

    public int getTotalRecipesCount() {
        return recipeRepository.findAll().size();
    }

    /**
     * Calcula el Food Cost Promedio (%) de toda la carta del restaurante.
     * Food Cost % = (Coste Ración / PVP sin IVA) * 100
     */
    public double getAverageFoodCostPercentage() {
        List<Recipe> recipes = recipeRepository.findAll();
        if (recipes.isEmpty()) return 0.0;

        double sumFoodCost = 0.0;
        int count = 0;

        for (Recipe r : recipes) {
            double pvpNoVat = r.getRealPriceWithoutVat();
            if (pvpNoVat > 0) {
                double foodCost = (r.calculateCostPerPortion() / pvpNoVat) * 100.0;
                sumFoodCost += foodCost;
                count++;
            }
        }

        return count > 0 ? sumFoodCost / count : 0.0;
    }

    /**
     * Devuelve el escandallo con mayor margen de beneficio real.
     */
    public Optional<Recipe> getHighestMarginRecipe() {
        return recipeRepository.findAll().stream()
                .filter(r -> r.getRealSellingPriceWithVat() > 0)
                .max(Comparator.comparingDouble(Recipe::calculateRealMarginPercentage));
    }

    /**
     * Devuelve el escandallo con menor margen de beneficio real.
     */
    public Optional<Recipe> getLowestMarginRecipe() {
        return recipeRepository.findAll().stream()
                .filter(r -> r.getRealSellingPriceWithVat() > 0)
                .min(Comparator.comparingDouble(Recipe::calculateRealMarginPercentage));
    }
}
