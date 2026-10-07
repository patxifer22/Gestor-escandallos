package com.escandallos.service;

import com.escandallos.model.Category;
import com.escandallos.model.Ingredient;
import com.escandallos.model.Recipe;
import com.escandallos.model.RecipeItem;
import com.escandallos.model.Unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CostCalculatorServiceTest {

    @Test
    @DisplayName("Cálculo de coste neto de ingrediente considerando % de merma")
    public void testIngredientNetCostCalculation() {
        // Solomillo 20 €/kg con 20% de merma -> 0.8 kg aprovechables por cada kg -> 20 / 0.8 = 25.00 €/kg neto
        Ingredient solomillo = new Ingredient("Solomillo", "Carnes", 20.0, Unit.KG, 20.0, "Proveedor A", "Ninguno");
        assertEquals(25.00, solomillo.getNetCostPerUnit(), 0.001);

        // Sin merma (0%)
        Ingredient aceite = new Ingredient("Aceite", "Aceites", 10.0, Unit.L, 0.0, "Proveedor B", "Ninguno");
        assertEquals(10.00, aceite.getNetCostPerUnit(), 0.001);
    }

    @Test
    @DisplayName("Cálculo de coste total de receta, ración y PVP sugerido con IVA")
    public void testRecipeCostingCalculation() {
        // Ingrediente A: 10€/kg, 0% merma -> 10€/kg
        Ingredient ingA = new Ingredient("Carne", "Carnes", 10.0, Unit.KG, 0.0, "P1", "N");
        // Ingrediente B: 4€/kg, 20% merma -> 4 / 0.8 = 5€/kg
        Ingredient ingB = new Ingredient("Verdura", "Verduras", 4.0, Unit.KG, 20.0, "P2", "N");

        // Receta para 4 raciones
        Recipe receta = new Recipe("Plato Combinado", Category.PRINCIPAL, 4, 75.0, 10.0);
        receta.addItem(new RecipeItem(ingA, 500, Unit.G)); // 0.5 kg * 10 = 5.0 €
        receta.addItem(new RecipeItem(ingB, 400, Unit.G)); // 0.4 kg * 5 = 2.0 €

        // Coste total materia prima = 5.0 + 2.0 = 7.0 €
        assertEquals(7.00, receta.calculateTotalCost(), 0.001);

        // Coste por ración (4 raciones) = 7.0 / 4 = 1.75 €
        assertEquals(1.75, receta.calculateCostPerPortion(), 0.001);

        // PVP sugerido sin IVA (75% margen) -> Margen factor = 1 - 0.75 = 0.25 -> 1.75 / 0.25 = 7.00 €
        assertEquals(7.00, receta.calculateSuggestedPriceWithoutVat(), 0.001);

        // PVP sugerido con IVA 10% -> 7.00 * 1.10 = 7.70 €
        assertEquals(7.70, receta.calculateSuggestedPriceWithVat(), 0.001);
    }
}
