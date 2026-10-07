package com.escandallos.model;

/**
 * Representa una línea de ingrediente dentro de una receta/escandallo.
 * Especifica el ingrediente utilizado, la cantidad bruta y la unidad empleada.
 */
public class RecipeItem {
    private Ingredient ingredient;
    private double quantity; // Cantidad bruta requerida en la receta
    private Unit unit;       // Unidad utilizada en la receta (ej. Gramos vs Kg de compra)

    public RecipeItem() {
    }

    public RecipeItem(Ingredient ingredient, double quantity, Unit unit) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
    }

    /**
     * Convierte la cantidad de la receta a la unidad base de compra del ingrediente.
     * Soporta conversiones automáticas de g -> kg y ml -> L.
     */
    public double getQuantityInIngredientUnit() {
        if (ingredient == null || ingredient.getUnit() == null || unit == null) {
            return quantity;
        }

        Unit baseUnit = ingredient.getUnit();
        if (unit == baseUnit) {
            return quantity;
        }

        // Conversiones de masa
        if (unit == Unit.G && baseUnit == Unit.KG) {
            return quantity / 1000.0;
        }
        if (unit == Unit.KG && baseUnit == Unit.G) {
            return quantity * 1000.0;
        }

        // Conversiones de volumen
        if (unit == Unit.ML && baseUnit == Unit.L) {
            return quantity / 1000.0;
        }
        if (unit == Unit.L && baseUnit == Unit.ML) {
            return quantity * 1000.0;
        }

        return quantity;
    }

    /**
     * Calcula el coste total de este ingrediente en la receta teniendo en cuenta la merma.
     */
    public double calculateTotalCost() {
        if (ingredient == null) return 0.0;
        double baseQuantity = getQuantityInIngredientUnit();
        double netCostPerUnit = ingredient.getNetCostPerUnit();
        return baseQuantity * netCostPerUnit;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
