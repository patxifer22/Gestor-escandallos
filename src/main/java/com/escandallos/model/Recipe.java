package com.escandallos.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Modelo de datos para un Escandallo / Receta.
 * Contiene la lista de ingredientes, el rendimiento en raciones,
 * y los parámetros financieros para el cálculo de costes y márgenes.
 */
public class Recipe {
    private String id;
    private String name;
    private Category category;
    private int portions; // Número de raciones del escandallo (yield)
    private double targetMarginPercentage; // Margen de beneficio deseado (%) (ej. 75.0)
    private double vatPercentage; // IVA aplicable (%) (ej. 10.0)
    private double realSellingPriceWithVat; // PVP real fijado en carta (con IVA incluido)
    private List<RecipeItem> items;
    private String notes;

    public Recipe() {
        this.id = UUID.randomUUID().toString();
        this.portions = 1;
        this.targetMarginPercentage = 70.0;
        this.vatPercentage = 10.0;
        this.items = new ArrayList<>();
        this.category = Category.PRINCIPAL;
    }

    public Recipe(String name, Category category, int portions, double targetMarginPercentage, double vatPercentage) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.category = category;
        this.portions = Math.max(1, portions);
        this.targetMarginPercentage = targetMarginPercentage;
        this.vatPercentage = vatPercentage;
        this.items = new ArrayList<>();
    }

    public void addItem(RecipeItem item) {
        if (item != null) {
            this.items.add(item);
        }
    }

    public void removeItem(RecipeItem item) {
        this.items.remove(item);
    }

    /**
     * Coste total de materia prima de todo el escandallo (todas las raciones).
     */
    public double calculateTotalCost() {
        double total = 0.0;
        for (RecipeItem item : items) {
            total += item.calculateTotalCost();
        }
        return total;
    }

    /**
     * Coste de materia prima por ración individual.
     */
    public double calculateCostPerPortion() {
        if (portions <= 0) return calculateTotalCost();
        return calculateTotalCost() / portions;
    }

    /**
     * PVP sugerido por ración (SIN IVA) basado en el margen deseado.
     * Formula: CosteRacion / (1 - (MargenDeseado / 100))
     */
    public double calculateSuggestedPriceWithoutVat() {
        double costPerPortion = calculateCostPerPortion();
        if (targetMarginPercentage >= 100.0) {
            return costPerPortion;
        }
        double marginFactor = 1.0 - (targetMarginPercentage / 100.0);
        if (marginFactor <= 0) return costPerPortion;
        return costPerPortion / marginFactor;
    }

    /**
     * PVP sugerido por ración (CON IVA) basado en el margen deseado.
     */
    public double calculateSuggestedPriceWithVat() {
        double priceNoVat = calculateSuggestedPriceWithoutVat();
        return priceNoVat * (1.0 + (vatPercentage / 100.0));
    }

    /**
     * PVP real sin IVA fijado por la cocina/restaurante.
     */
    public double getRealPriceWithoutVat() {
        if (vatPercentage <= -100) return realSellingPriceWithVat;
        return realSellingPriceWithVat / (1.0 + (vatPercentage / 100.0));
    }

    /**
     * Beneficio neto por ración según el PVP real fijado en carta.
     */
    public double calculateNetProfitPerPortion() {
        double priceNoVat = getRealPriceWithoutVat();
        double costPerPortion = calculateCostPerPortion();
        return priceNoVat - costPerPortion;
    }

    /**
     * Margen real de beneficio (%) obtenido con el PVP actual en carta.
     */
    public double calculateRealMarginPercentage() {
        double priceNoVat = getRealPriceWithoutVat();
        if (priceNoVat <= 0) return 0.0;
        double netProfit = calculateNetProfitPerPortion();
        return (netProfit / priceNoVat) * 100.0;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public int getPortions() { return portions; }
    public void setPortions(int portions) { this.portions = Math.max(1, portions); }

    public double getTargetMarginPercentage() { return targetMarginPercentage; }
    public void setTargetMarginPercentage(double targetMarginPercentage) { this.targetMarginPercentage = targetMarginPercentage; }

    public double getVatPercentage() { return vatPercentage; }
    public void setVatPercentage(double vatPercentage) { this.vatPercentage = vatPercentage; }

    public double getRealSellingPriceWithVat() { return realSellingPriceWithVat; }
    public void setRealSellingPriceWithVat(double realSellingPriceWithVat) { this.realSellingPriceWithVat = realSellingPriceWithVat; }

    public List<RecipeItem> getItems() { return items; }
    public void setItems(List<RecipeItem> items) { this.items = items != null ? items : new ArrayList<>(); }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " (" + category.getDisplayName() + ")";
    }
}
