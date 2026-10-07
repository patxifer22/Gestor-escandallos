package com.escandallos.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Modelo de datos para un Ingrediente.
 * Incluye precio de compra, unidad de medida, proveedor y porcentaje de merma (desperdicio).
 */
public class Ingredient {
    private String id;
    private String name;
    private String category;
    private double purchasePrice; // Precio de compra (€ / unidad)
    private Unit unit;           // Unidad de compra (KG, L, UD...)
    private double wastePercentage; // % de merma / desperdicio (0 a 99%)
    private String supplier;     // Nombre del proveedor
    private String allergens;    // Lista de alérgenos

    public Ingredient() {
        this.id = UUID.randomUUID().toString();
        this.wastePercentage = 0.0;
        this.unit = Unit.KG;
    }

    public Ingredient(String name, String category, double purchasePrice, Unit unit, double wastePercentage, String supplier, String allergens) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.category = category;
        this.purchasePrice = purchasePrice;
        this.unit = unit;
        this.wastePercentage = wastePercentage;
        this.supplier = supplier;
        this.allergens = allergens;
    }

    public Ingredient(String id, String name, String category, double purchasePrice, Unit unit, double wastePercentage, String supplier, String allergens) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.purchasePrice = purchasePrice;
        this.unit = unit;
        this.wastePercentage = wastePercentage;
        this.supplier = supplier;
        this.allergens = allergens;
    }

    /**
     * Calcula el precio real/neto del ingrediente aprovechable tras aplicar la merma.
     * Ejemplo: Si 1 kg de solomillo cuesta 20€ y tiene un 20% de merma (grasa/huesos),
     * el peso aprovechable es 0.8 kg. Por tanto, el kg útil cuesta 20 / (1 - 0.20) = 25.00 €.
     */
    public double getNetCostPerUnit() {
        if (wastePercentage >= 100.0) {
            return purchasePrice;
        }
        double usableFactor = 1.0 - (wastePercentage / 100.0);
        if (usableFactor <= 0) return purchasePrice;
        return purchasePrice / usableFactor;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(double purchasePrice) { this.purchasePrice = purchasePrice; }

    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }

    public double getWastePercentage() { return wastePercentage; }
    public void setWastePercentage(double wastePercentage) { this.wastePercentage = wastePercentage; }

    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }

    public String getAllergens() { return allergens; }
    public void setAllergens(String allergens) { this.allergens = allergens; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " (" + String.format("%.2f", purchasePrice) + " €/" + unit.getSymbol() + ")";
    }
}
