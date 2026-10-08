package com.escandallos.repository;

import com.escandallos.model.Category;
import com.escandallos.model.Ingredient;
import com.escandallos.model.Recipe;
import com.escandallos.model.RecipeItem;
import com.escandallos.model.Unit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseRepositoryTest {

    private static IngredientRepository ingredientRepo;
    private static RecipeRepository recipeRepo;

    @BeforeAll
    public static void setUp() {
        DatabaseManager.initDatabase();
        ingredientRepo = new IngredientRepository();
        recipeRepo = new RecipeRepository();
    }

    @Test
    @DisplayName("Debe persistir y recuperar un ingrediente en SQLite")
    public void testSaveAndRetrieveIngredient() {
        Ingredient ing = new Ingredient("Trufa Negra", "Hongos", 800.0, Unit.KG, 5.0, "Trufas del Norte", "Ninguno");
        ingredientRepo.save(ing);

        Optional<Ingredient> found = ingredientRepo.findById(ing.getId());
        assertTrue(found.isPresent());
        assertEquals("Trufa Negra", found.get().getName());
        assertEquals(800.0, found.get().getPurchasePrice(), 0.001);
        assertEquals(5.0, found.get().getWastePercentage(), 0.001);

        // Limpieza
        ingredientRepo.deleteById(ing.getId());
        assertFalse(ingredientRepo.findById(ing.getId()).isPresent());
    }

    @Test
    @DisplayName("Debe persistir y recuperar un escandallo con sus ítems relacionales en SQLite")
    public void testSaveAndRetrieveRecipeWithItems() {
        Ingredient ing1 = new Ingredient("Harina de Trigo", "Cereales", 1.20, Unit.KG, 0.0, "Molino Sur", "Gluten");
        Ingredient ing2 = new Ingredient("Mantequilla", "Lácteos", 6.50, Unit.KG, 2.0, "Lácteos del Valle", "Lactosa");
        ingredientRepo.save(ing1);
        ingredientRepo.save(ing2);

        Recipe recipe = new Recipe("Masa Hojaldre", Category.POSTRE, 10, 70.0, 10.0);
        recipe.setRealSellingPriceWithVat(15.0);
        recipe.addItem(new RecipeItem(ing1, 500, Unit.G));
        recipe.addItem(new RecipeItem(ing2, 250, Unit.G));

        recipeRepo.save(recipe);

        Optional<Recipe> retrieved = recipeRepo.findById(recipe.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Masa Hojaldre", retrieved.get().getName());
        assertEquals(10, retrieved.get().getPortions());
        assertEquals(2, retrieved.get().getItems().size());
        assertTrue(retrieved.get().calculateTotalCost() > 0);

        // Limpieza
        recipeRepo.deleteById(recipe.getId());
        assertFalse(recipeRepo.findById(recipe.getId()).isPresent());
        ingredientRepo.deleteById(ing1.getId());
        ingredientRepo.deleteById(ing2.getId());
    }
}
