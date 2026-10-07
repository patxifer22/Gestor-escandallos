package com.escandallos.ui.views;

import com.escandallos.model.Category;
import com.escandallos.model.Ingredient;
import com.escandallos.model.Recipe;
import com.escandallos.model.RecipeItem;
import com.escandallos.model.Unit;
import com.escandallos.repository.IngredientRepository;
import com.escandallos.repository.RecipeRepository;
import com.escandallos.util.CurrencyFormatter;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Vista JavaFX interactiva para el diseño de escandallos, fichas técnicas y cálculo de costes y PVP.
 */
public class RecipeView extends VBox {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final Runnable onDataChanged;

    private final TableView<Recipe> table;
    private final ObservableList<Recipe> recipeData;
    private final TextField searchField;

    public RecipeView(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, Runnable onDataChanged) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.onDataChanged = onDataChanged;
        this.recipeData = FXCollections.observableArrayList();

        setSpacing(15);
        setPadding(new Insets(20, 24, 20, 24));

        // Header & Toolbar
        VBox titleBox = new VBox(4);
        Label titleLabel = new Label("Creador de Escandallos y Fichas Técnicas");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");
        Label subtitleLabel = new Label("Calcule el coste exacto por ración, sugiera PVP con márgenes de beneficio e IVA aplicable");
        subtitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #a6adc8;");
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);

        HBox toolbar = new HBox(10);
        toolbar.setStyle("-fx-alignment: CENTER_RIGHT;");

        searchField = new TextField();
        searchField.setPromptText("🔍 Buscar escandallo...");
        searchField.setPrefWidth(220);
        searchField.textProperty().addListener((obs, o, n) -> filter(n));

        Button btnAdd = new Button("➕ Nuevo Escandallo");
        btnAdd.getStyleClass().add("button-primary");
        btnAdd.setOnAction(e -> showRecipeDialog(null));

        Button btnEdit = new Button("📋 Abrir Ficha Técnica / Editar");
        btnEdit.setOnAction(e -> editSelected());

        Button btnDelete = new Button("🗑️ Eliminar");
        btnDelete.getStyleClass().add("button-danger");
        btnDelete.setOnAction(e -> deleteSelected());

        HBox searchContainer = new HBox(8, new Label("Filtrar:"), searchField);
        searchContainer.setStyle("-fx-alignment: CENTER_LEFT;");
        HBox.setHgrow(searchContainer, Priority.ALWAYS);

        toolbar.getChildren().addAll(searchContainer, btnAdd, btnEdit, btnDelete);

        // Tabla JavaFX Escandallos
        table = new TableView<>(recipeData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Recipe, String> nameCol = new TableColumn<>("Escandallo");
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));

        TableColumn<Recipe, String> catCol = new TableColumn<>("Categoría");
        catCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategory().getDisplayName()));

        TableColumn<Recipe, String> portionsCol = new TableColumn<>("Raciones");
        portionsCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getPortions())));

        TableColumn<Recipe, String> totalCostCol = new TableColumn<>("Coste Total");
        totalCostCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatCurrency(cell.getValue().calculateTotalCost())));

        TableColumn<Recipe, String> costPortionCol = new TableColumn<>("Coste / Ración");
        costPortionCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatCurrency(cell.getValue().calculateCostPerPortion())));

        TableColumn<Recipe, String> pvpSuggCol = new TableColumn<>("PVP Sugerido (c/IVA)");
        pvpSuggCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatCurrency(cell.getValue().calculateSuggestedPriceWithVat())));

        TableColumn<Recipe, String> pvpRealCol = new TableColumn<>("PVP Carta (c/IVA)");
        pvpRealCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatCurrency(cell.getValue().getRealSellingPriceWithVat())));

        TableColumn<Recipe, String> profitCol = new TableColumn<>("Beneficio / Ración");
        profitCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatCurrency(cell.getValue().calculateNetProfitPerPortion())));

        TableColumn<Recipe, String> marginCol = new TableColumn<>("Margen Real %");
        marginCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatPercent(cell.getValue().calculateRealMarginPercentage())));

        table.getColumns().addAll(nameCol, catCol, portionsCol, totalCostCol, costPortionCol, pvpSuggCol, pvpRealCol, profitCol, marginCol);
        VBox.setVgrow(table, Priority.ALWAYS);

        getChildren().addAll(titleBox, toolbar, table);

        refreshTable();
    }

    public void refreshTable() {
        recipeData.setAll(recipeRepository.findAll());
    }

    private void filter(String query) {
        recipeData.setAll(recipeRepository.searchByName(query));
    }

    private void editSelected() {
        Recipe selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Atención", "Seleccione un escandallo de la tabla.", Alert.AlertType.WARNING);
            return;
        }
        showRecipeDialog(selected);
    }

    private void deleteSelected() {
        Recipe selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Atención", "Seleccione un escandallo para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de eliminar '" + selected.getName() + "'?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirmar Eliminación");
        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.YES) {
                recipeRepository.deleteById(selected.getId());
                refreshTable();
                if (onDataChanged != null) onDataChanged.run();
            }
        });
    }

    private void showRecipeDialog(Recipe recipeToEdit) {
        boolean isEdit = recipeToEdit != null;
        Recipe workingRecipe = isEdit ? recipeToEdit : new Recipe("Nuevo Escandallo", Category.PRINCIPAL, 4, 70.0, 10.0);

        Dialog<Recipe> dialog = new Dialog<>();
        dialog.setTitle(isEdit ? "Ficha Técnica y Escandallo: " + workingRecipe.getName() : "Crear Escandallo");
        dialog.getDialogPane().setPrefSize(850, 680);

        ButtonType btnSaveType = new ButtonType("Guardar Escandallo", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSaveType, ButtonType.CANCEL);

        VBox mainLayout = new VBox(12);
        mainLayout.setPadding(new Insets(15));

        // Formulario superior
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(workingRecipe.getName());
        ComboBox<Category> catCombo = new ComboBox<>(FXCollections.observableArrayList(Category.values()));
        catCombo.setValue(workingRecipe.getCategory());

        Spinner<Integer> portionsSpinner = new Spinner<>(1, 500, workingRecipe.getPortions());
        TextField marginField = new TextField(String.valueOf(workingRecipe.getTargetMarginPercentage()));
        TextField vatField = new TextField(String.valueOf(workingRecipe.getVatPercentage()));
        TextField pvpRealField = new TextField(String.valueOf(workingRecipe.getRealSellingPriceWithVat()));

        grid.add(new Label("Nombre Escandallo:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Categoría:"), 2, 0);
        grid.add(catCombo, 3, 0);

        grid.add(new Label("Raciones (Yield):"), 0, 1);
        grid.add(portionsSpinner, 1, 1);
        grid.add(new Label("Margen Objetivo (%):"), 2, 1);
        grid.add(marginField, 3, 1);

        grid.add(new Label("IVA Aplicable (%):"), 0, 2);
        grid.add(vatField, 1, 2);
        grid.add(new Label("PVP Real Carta (€ w/IVA):"), 2, 2);
        grid.add(pvpRealField, 3, 2);

        // Tabla de ítems en la receta
        ObservableList<RecipeItem> itemsData = FXCollections.observableArrayList(workingRecipe.getItems());
        TableView<RecipeItem> itemsTable = new TableView<>(itemsData);
        itemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<RecipeItem, String> ingNameCol = new TableColumn<>("Ingrediente");
        ingNameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIngredient().getName()));

        TableColumn<RecipeItem, String> ingNetPriceCol = new TableColumn<>("Precio Neto Compra");
        ingNetPriceCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatCurrency(cell.getValue().getIngredient().getNetCostPerUnit()) + " / " + cell.getValue().getIngredient().getUnit().getSymbol()));

        TableColumn<RecipeItem, String> ingQtyCol = new TableColumn<>("Cantidad Usada");
        ingQtyCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getQuantity() + " " + cell.getValue().getUnit().getSymbol()));

        TableColumn<RecipeItem, String> ingCostCol = new TableColumn<>("Coste Resultante");
        ingCostCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatCurrency(cell.getValue().calculateTotalCost())));

        itemsTable.getColumns().addAll(ingNameCol, ingNetPriceCol, ingQtyCol, ingCostCol);
        VBox.setVgrow(itemsTable, Priority.ALWAYS);

        // Labels resumen financiero
        Label lblTotalCost = new Label();
        Label lblCostPortion = new Label();
        Label lblSuggestedPvp = new Label();
        Label lblProfitPortion = new Label();
        Label lblRealMargin = new Label();
        lblRealMargin.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        GridPane summaryGrid = new GridPane();
        summaryGrid.setStyle("-fx-background-color: #181825; -fx-padding: 12px; -fx-background-radius: 8px;");
        summaryGrid.setHgap(20);
        summaryGrid.setVgap(6);

        summaryGrid.add(lblTotalCost, 0, 0);
        summaryGrid.add(lblCostPortion, 1, 0);
        summaryGrid.add(lblSuggestedPvp, 2, 0);
        summaryGrid.add(lblProfitPortion, 0, 1);
        summaryGrid.add(lblRealMargin, 1, 1);

        Runnable recalculate = () -> {
            workingRecipe.setName(nameField.getText().trim());
            workingRecipe.setCategory(catCombo.getValue());
            workingRecipe.setPortions(portionsSpinner.getValue());
            try {
                workingRecipe.setTargetMarginPercentage(Double.parseDouble(marginField.getText().replace(",", ".")));
                workingRecipe.setVatPercentage(Double.parseDouble(vatField.getText().replace(",", ".")));
                workingRecipe.setRealSellingPriceWithVat(Double.parseDouble(pvpRealField.getText().replace(",", ".")));
            } catch (Exception ignored) {}
            workingRecipe.setItems(new ArrayList<>(itemsData));

            lblTotalCost.setText("Coste Mat. Prima: " + CurrencyFormatter.formatCurrency(workingRecipe.calculateTotalCost()));
            lblCostPortion.setText("Coste / Ración: " + CurrencyFormatter.formatCurrency(workingRecipe.calculateCostPerPortion()));
            lblSuggestedPvp.setText("PVP Sugerido (c/IVA): " + CurrencyFormatter.formatCurrency(workingRecipe.calculateSuggestedPriceWithVat()));
            lblProfitPortion.setText("Beneficio / Ración: " + CurrencyFormatter.formatCurrency(workingRecipe.calculateNetProfitPerPortion()));

            double realMargin = workingRecipe.calculateRealMarginPercentage();
            lblRealMargin.setText("Margen Real: " + CurrencyFormatter.formatPercent(realMargin));
            lblRealMargin.setStyle(realMargin >= 70.0 ? "-fx-text-fill: #a6e3a1; -fx-font-weight: bold;" : (realMargin >= 50.0 ? "-fx-text-fill: #fab387; -fx-font-weight: bold;" : "-fx-text-fill: #f38ba8; -fx-font-weight: bold;"));
        };

        nameField.textProperty().addListener((o, oldV, newV) -> recalculate.run());
        marginField.textProperty().addListener((o, oldV, newV) -> recalculate.run());
        vatField.textProperty().addListener((o, oldV, newV) -> recalculate.run());
        pvpRealField.textProperty().addListener((o, oldV, newV) -> recalculate.run());
        portionsSpinner.valueProperty().addListener((o, oldV, newV) -> recalculate.run());

        // Toolbar de gestión de ítems
        Button btnAddItem = new Button("➕ Añadir Ingrediente");
        btnAddItem.setOnAction(e -> {
            List<Ingredient> ingredients = ingredientRepository.findAll();
            if (ingredients.isEmpty()) {
                showAlert("Atención", "No hay ingredientes guardados.", Alert.AlertType.WARNING);
                return;
            }

            Dialog<RecipeItem> itemDialog = new Dialog<>();
            itemDialog.setTitle("Añadir Ingrediente al Escandallo");
            itemDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            GridPane itemGrid = new GridPane();
            itemGrid.setHgap(10);
            itemGrid.setVgap(10);

            ComboBox<Ingredient> ingCombo = new ComboBox<>(FXCollections.observableArrayList(ingredients));
            ingCombo.setValue(ingredients.get(0));
            TextField qtyInput = new TextField("100");
            ComboBox<Unit> unitInput = new ComboBox<>(FXCollections.observableArrayList(Unit.values()));
            unitInput.setValue(Unit.G);

            itemGrid.add(new Label("Ingrediente:"), 0, 0);
            itemGrid.add(ingCombo, 1, 0);
            itemGrid.add(new Label("Cantidad:"), 0, 1);
            itemGrid.add(qtyInput, 1, 1);
            itemGrid.add(new Label("Unidad Usada:"), 0, 2);
            itemGrid.add(unitInput, 1, 2);

            itemDialog.getDialogPane().setContent(itemGrid);
            itemDialog.setResultConverter(btn -> {
                if (btn == ButtonType.OK) {
                    try {
                        double qty = Double.parseDouble(qtyInput.getText().replace(",", "."));
                        return new RecipeItem(ingCombo.getValue(), qty, unitInput.getValue());
                    } catch (Exception ex) {
                        return null;
                    }
                }
                return null;
            });

            itemDialog.showAndWait().ifPresent(item -> {
                itemsData.add(item);
                recalculate.run();
            });
        });

        Button btnRemoveItem = new Button("➖ Eliminar Ingrediente");
        btnRemoveItem.setOnAction(e -> {
            RecipeItem sel = itemsTable.getSelectionModel().getSelectedItem();
            if (sel != null) {
                itemsData.remove(sel);
                recalculate.run();
            }
        });

        HBox itemToolbar = new HBox(10, btnAddItem, btnRemoveItem);

        mainLayout.getChildren().addAll(grid, itemToolbar, itemsTable, summaryGrid);
        dialog.getDialogPane().setContent(mainLayout);

        recalculate.run();

        dialog.setResultConverter(btn -> {
            if (btn == btnSaveType) {
                String name = nameField.getText().trim();
                if (name.isEmpty()) return null;

                workingRecipe.setName(name);
                workingRecipe.setCategory(catCombo.getValue());
                workingRecipe.setPortions(portionsSpinner.getValue());
                workingRecipe.setItems(new ArrayList<>(itemsData));
                return workingRecipe;
            }
            return null;
        });

        Optional<Recipe> result = dialog.showAndWait();
        result.ifPresent(r -> {
            recipeRepository.save(r);
            refreshTable();
            if (onDataChanged != null) onDataChanged.run();
        });
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
