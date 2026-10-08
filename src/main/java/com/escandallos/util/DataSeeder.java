package com.escandallos.util;

import com.escandallos.model.Category;
import com.escandallos.model.Ingredient;
import com.escandallos.model.Recipe;
import com.escandallos.model.RecipeItem;
import com.escandallos.model.Unit;
import com.escandallos.repository.IngredientRepository;
import com.escandallos.repository.RecipeRepository;

import java.util.List;

/**
 * Carga datos iniciales de demostración si la base de datos SQLite está vacía.
 */
public class DataSeeder {

    public static void seedIfEmpty(IngredientRepository ingredientRepo, RecipeRepository recipeRepo) {
        List<Ingredient> ingredients = ingredientRepo.findAll();
        if (!ingredients.isEmpty()) {
            return; // Ya hay datos almacenados
        }

        System.out.println("Sembrando datos de demostración para escandallos...");

        // 1. Ingredientes
        Ingredient solomillo = new Ingredient("Solomillo de Ternera", "Carnes", 24.50, Unit.KG, 15.0, "Cárnicas Garay", "Ninguno");
        Ingredient patata = new Ingredient("Patata Monalisa", "Verduras", 1.20, Unit.KG, 18.0, "Huertas Vega", "Ninguno");
        Ingredient aove = new Ingredient("Aceite Oliva Virgen Extra", "Aceites", 8.90, Unit.L, 0.0, "Olivas del Sur", "Ninguno");
        Ingredient nata = new Ingredient("Nata 35% M.G.", "Lácteos", 3.80, Unit.L, 2.0, "Lácteos Central", "Lactosa");
        Ingredient pimienta = new Ingredient("Pimienta Negra Grano", "Especias", 18.00, Unit.KG, 0.0, "Especias El Reloj", "Ninguno");
        Ingredient vino = new Ingredient("Vino Tinto Crianza", "Bebidas", 4.50, Unit.L, 0.0, "Bodegas Rioja", "Sulfitos");
        Ingredient arroz = new Ingredient("Arroz Bomba", "Cereales", 3.20, Unit.KG, 0.0, "Grano de Oro", "Ninguno");
        Ingredient gamba = new Ingredient("Gamba Roja de Dénia", "Pescados y Mariscos", 45.00, Unit.KG, 35.0, "Pescados Paca", "Crustáceos");
        Ingredient mascarpone = new Ingredient("Queso Mascarpone", "Lácteos", 7.50, Unit.KG, 0.0, "Lácteos Italia", "Lactosa");
        Ingredient huevo = new Ingredient("Huevo Campero XL", "Huevos", 0.25, Unit.UD, 10.0, "Avícola Real", "Huevo");

        ingredientRepo.save(solomillo);
        ingredientRepo.save(patata);
        ingredientRepo.save(aove);
        ingredientRepo.save(nata);
        ingredientRepo.save(pimienta);
        ingredientRepo.save(vino);
        ingredientRepo.save(arroz);
        ingredientRepo.save(gamba);
        ingredientRepo.save(mascarpone);
        ingredientRepo.save(huevo);

        // 2. Escandallos / Recetas
        // Receta 1: Solomillo de Ternera a la Pimienta
        Recipe recetaSolomillo = new Recipe("Solomillo de Ternera a la Pimienta", Category.PRINCIPAL, 4, 75.0, 10.0);
        recetaSolomillo.setRealSellingPriceWithVat(24.00);
        recetaSolomillo.setNotes("Marcar el solomillo a fuego fuerte. Reducir la nata con el vino y la pimienta recién molida.");
        recetaSolomillo.addItem(new RecipeItem(solomillo, 800, Unit.G));
        recetaSolomillo.addItem(new RecipeItem(patata, 600, Unit.G));
        recetaSolomillo.addItem(new RecipeItem(nata, 250, Unit.ML));
        recetaSolomillo.addItem(new RecipeItem(pimienta, 20, Unit.G));
        recetaSolomillo.addItem(new RecipeItem(vino, 100, Unit.ML));
        recetaSolomillo.addItem(new RecipeItem(aove, 50, Unit.ML));

        // Receta 2: Paella de Gamba Roja
        Recipe recetaPaella = new Recipe("Paella de Gamba Roja de Dénia", Category.PRINCIPAL, 2, 70.0, 10.0);
        recetaPaella.setRealSellingPriceWithVat(22.50);
        recetaPaella.setNotes("Sofreír las cabezas de las gambas para extraer el jugo. Cocción del arroz 18 minutos.");
        recetaPaella.addItem(new RecipeItem(arroz, 250, Unit.G));
        recetaPaella.addItem(new RecipeItem(gamba, 400, Unit.G));
        recetaPaella.addItem(new RecipeItem(aove, 80, Unit.ML));

        recipeRepo.save(recetaSolomillo);
        recipeRepo.save(recetaPaella);

        System.out.println("Sembrado de datos iniciales completado.");
    }
}
