package com.escandallos.ui.views;

import com.escandallos.model.Recipe;
import com.escandallos.repository.IngredientRepository;
import com.escandallos.repository.RecipeRepository;
import com.escandallos.service.CostCalculatorService;
import com.escandallos.ui.components.MetricCard;
import com.escandallos.util.CurrencyFormatter;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Optional;

/**
 * Vista JavaFX del Dashboard con métricas ejecutivas de rentabilidad y tabla de escandallos.
 */
public class DashboardView extends VBox {
    private final CostCalculatorService calculatorService;
    private final RecipeRepository recipeRepository;

    private final MetricCard cardIngredients;
    private final MetricCard cardRecipes;
    private final MetricCard cardFoodCost;
    private final MetricCard cardTopRecipe;

    private final TableView<Recipe> overviewTable;
    private final ObservableList<Recipe> recipeData;

    public DashboardView(IngredientRepository ingredientRepo, RecipeRepository recipeRepo) {
        this.recipeRepository = recipeRepo;
        this.calculatorService = new CostCalculatorService(ingredientRepo, recipeRepo);
        this.recipeData = FXCollections.observableArrayList();

        setSpacing(20);
        setPadding(new Insets(20, 24, 20, 24));

        // Header Text
        VBox headerBox = new VBox(4);
        Label titleLabel = new Label("Resumen del Restaurante y Control de Costes");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");
        Label subtitleLabel = new Label("Visión general de materia prima, escandallos, márgenes y rentabilidad");
        subtitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #a6adc8;");
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);

        // 4 KPI Cards Grid
        GridPane cardsGrid = new GridPane();
        cardsGrid.setHgap(16);
        cardsGrid.setVgap(16);

        cardIngredients = new MetricCard("Ingredientes Registrados", "0", "En base de datos", "#89b4fa");
        cardRecipes = new MetricCard("Escandallos Activos", "0", "Recetas en carta", "#cba6f7");
        cardFoodCost = new MetricCard("Food Cost Promedio", "0,0 %", "Objetivo ideal: 28-32%", "#fab387");
        cardTopRecipe = new MetricCard("Mayor Rentabilidad", "-", "Mejor margen neto", "#a6e3a1");

        GridPane.setHgrow(cardIngredients, Priority.ALWAYS);
        GridPane.setHgrow(cardRecipes, Priority.ALWAYS);
        GridPane.setHgrow(cardFoodCost, Priority.ALWAYS);
        GridPane.setHgrow(cardTopRecipe, Priority.ALWAYS);

        cardsGrid.add(cardIngredients, 0, 0);
        cardsGrid.add(cardRecipes, 1, 0);
        cardsGrid.add(cardFoodCost, 2, 0);
        cardsGrid.add(cardTopRecipe, 3, 0);

        // Tabla de resumen
        Label tableTitle = new Label("Resumen Financiero de Escandallos");
        tableTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");

        overviewTable = new TableView<>(recipeData);
        overviewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Recipe, String> nameCol = new TableColumn<>("Escandallo");
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));

        TableColumn<Recipe, String> catCol = new TableColumn<>("Categoría");
        catCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategory().getDisplayName()));

        TableColumn<Recipe, String> yieldCol = new TableColumn<>("Raciones");
        yieldCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPortions() + " raciones"));

        TableColumn<Recipe, String> costCol = new TableColumn<>("Coste / Ración");
        costCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatCurrency(cell.getValue().calculateCostPerPortion())));

        TableColumn<Recipe, String> priceCol = new TableColumn<>("PVP Carta (c/IVA)");
        priceCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatCurrency(cell.getValue().getRealSellingPriceWithVat())));

        TableColumn<Recipe, String> marginCol = new TableColumn<>("Margen Real %");
        marginCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatPercent(cell.getValue().calculateRealMarginPercentage())));

        TableColumn<Recipe, String> statusCol = new TableColumn<>("Estado Rentabilidad");
        statusCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().calculateRealMarginPercentage() >= 70.0 ? "✅ Excelente (≥70%)" : (cell.getValue().calculateRealMarginPercentage() >= 50.0 ? "⚠️ Aceptable" : "❌ Margen Bajo")));

        overviewTable.getColumns().addAll(nameCol, catCol, yieldCol, costCol, priceCol, marginCol, statusCol);

        VBox.setVgrow(overviewTable, Priority.ALWAYS);

        getChildren().addAll(headerBox, cardsGrid, tableTitle, overviewTable);

        refreshData();
    }

    public void refreshData() {
        cardIngredients.setValue(String.valueOf(calculatorService.getTotalIngredientsCount()));
        cardRecipes.setValue(String.valueOf(calculatorService.getTotalRecipesCount()));

        double avgFoodCost = calculatorService.getAverageFoodCostPercentage();
        cardFoodCost.setValue(CurrencyFormatter.formatPercent(avgFoodCost));

        Optional<Recipe> topRecipe = calculatorService.getHighestMarginRecipe();
        if (topRecipe.isPresent()) {
            cardTopRecipe.setValue(CurrencyFormatter.formatPercent(topRecipe.get().calculateRealMarginPercentage()));
            cardTopRecipe.setSubtitle(topRecipe.get().getName());
        } else {
            cardTopRecipe.setValue("-");
            cardTopRecipe.setSubtitle("Sin recetas");
        }

        recipeData.setAll(recipeRepository.findAll());
    }
}
