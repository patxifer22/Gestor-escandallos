package com.escandallos.ui.views;

import com.escandallos.model.Ingredient;
import com.escandallos.model.Unit;
import com.escandallos.repository.IngredientRepository;
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

import java.util.Optional;

/**
 * Vista JavaFX de gestión de ingredientes con precios de compra, mermas y proveedores.
 */
public class IngredientView extends VBox {
    private final IngredientRepository repository;
    private final Runnable onDataChanged;

    private final TableView<Ingredient> table;
    private final ObservableList<Ingredient> ingredientData;
    private final TextField searchField;

    public IngredientView(IngredientRepository repository, Runnable onDataChanged) {
        this.repository = repository;
        this.onDataChanged = onDataChanged;
        this.ingredientData = FXCollections.observableArrayList();

        setSpacing(15);
        setPadding(new Insets(20, 24, 20, 24));

        // Header & Toolbar
        VBox titleBox = new VBox(4);
        Label titleLabel = new Label("Base de Datos de Ingredientes y Mermas");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");
        Label subtitleLabel = new Label("Gestione precios de compra, unidades de medida y porcentaje de desperdicio por ingrediente");
        subtitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #a6adc8;");
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);

        HBox toolbar = new HBox(10);
        toolbar.setStyle("-fx-alignment: CENTER_RIGHT;");

        searchField = new TextField();
        searchField.setPromptText("🔍 Buscar ingrediente...");
        searchField.setPrefWidth(220);
        searchField.textProperty().addListener((obs, oldText, newText) -> filter(newText));

        Button btnAdd = new Button("➕ Nuevo Ingrediente");
        btnAdd.getStyleClass().add("button-primary");
        btnAdd.setOnAction(e -> showIngredientDialog(null));

        Button btnEdit = new Button("✏️ Editar");
        btnEdit.setOnAction(e -> editSelected());

        Button btnDelete = new Button("🗑️ Eliminar");
        btnDelete.getStyleClass().add("button-danger");
        btnDelete.setOnAction(e -> deleteSelected());

        HBox searchContainer = new HBox(8, new Label("Filtrar:"), searchField);
        searchContainer.setStyle("-fx-alignment: CENTER_LEFT;");
        HBox.setHgrow(searchContainer, Priority.ALWAYS);

        toolbar.getChildren().addAll(searchContainer, btnAdd, btnEdit, btnDelete);

        // Tabla JavaFX
        table = new TableView<>(ingredientData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Ingredient, String> nameCol = new TableColumn<>("Nombre");
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));

        TableColumn<Ingredient, String> catCol = new TableColumn<>("Categoría");
        catCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategory()));

        TableColumn<Ingredient, String> priceCol = new TableColumn<>("Precio Bruto");
        priceCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatCurrency(cell.getValue().getPurchasePrice()) + " / " + cell.getValue().getUnit().getSymbol()));

        TableColumn<Ingredient, String> unitCol = new TableColumn<>("Unidad");
        unitCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUnit().getDisplayName()));

        TableColumn<Ingredient, String> wasteCol = new TableColumn<>("Merma %");
        wasteCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatPercent(cell.getValue().getWastePercentage())));

        TableColumn<Ingredient, String> netPriceCol = new TableColumn<>("Coste Real Neto");
        netPriceCol.setCellValueFactory(cell -> new SimpleStringProperty(CurrencyFormatter.formatCurrency(cell.getValue().getNetCostPerUnit()) + " / " + cell.getValue().getUnit().getSymbol()));

        TableColumn<Ingredient, String> supplierCol = new TableColumn<>("Proveedor");
        supplierCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSupplier() != null ? cell.getValue().getSupplier() : "-"));

        TableColumn<Ingredient, String> allergensCol = new TableColumn<>("Alérgenos");
        allergensCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAllergens() != null ? cell.getValue().getAllergens() : "-"));

        table.getColumns().addAll(nameCol, catCol, priceCol, unitCol, wasteCol, netPriceCol, supplierCol, allergensCol);
        VBox.setVgrow(table, Priority.ALWAYS);

        getChildren().addAll(titleBox, toolbar, table);

        refreshTable();
    }

    public void refreshTable() {
        ingredientData.setAll(repository.findAll());
    }

    private void filter(String query) {
        ingredientData.setAll(repository.searchByName(query));
    }

    private void editSelected() {
        Ingredient selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Atención", "Seleccione un ingrediente de la tabla.", Alert.AlertType.WARNING);
            return;
        }
        showIngredientDialog(selected);
    }

    private void deleteSelected() {
        Ingredient selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Atención", "Seleccione un ingrediente para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de eliminar '" + selected.getName() + "'?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirmar Eliminación");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                repository.deleteById(selected.getId());
                refreshTable();
                if (onDataChanged != null) onDataChanged.run();
            }
        });
    }

    private void showIngredientDialog(Ingredient ingredientToEdit) {
        boolean isEdit = ingredientToEdit != null;
        Dialog<Ingredient> dialog = new Dialog<>();
        dialog.setTitle(isEdit ? "Editar Ingrediente" : "Nuevo Ingrediente");
        dialog.setHeaderText("Introduzca los datos del ingrediente y su % de merma.");

        ButtonType btnSaveType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSaveType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(isEdit ? ingredientToEdit.getName() : "");
        TextField categoryField = new TextField(isEdit ? ingredientToEdit.getCategory() : "General");
        TextField priceField = new TextField(isEdit ? String.valueOf(ingredientToEdit.getPurchasePrice()) : "0.0");
        ComboBox<Unit> unitCombo = new ComboBox<>(FXCollections.observableArrayList(Unit.values()));
        unitCombo.setValue(isEdit ? ingredientToEdit.getUnit() : Unit.KG);

        TextField wasteField = new TextField(isEdit ? String.valueOf(ingredientToEdit.getWastePercentage()) : "0.0");
        Label netCostLabel = new Label(isEdit ? CurrencyFormatter.formatCurrency(ingredientToEdit.getNetCostPerUnit()) : "0,00 €");
        netCostLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #a6e3a1; -fx-font-size: 14px;");

        TextField supplierField = new TextField(isEdit ? ingredientToEdit.getSupplier() : "");
        TextField allergensField = new TextField(isEdit ? ingredientToEdit.getAllergens() : "Ninguno");

        Runnable updateNetCost = () -> {
            try {
                double price = Double.parseDouble(priceField.getText().replace(",", "."));
                double waste = Double.parseDouble(wasteField.getText().replace(",", "."));
                double usableFactor = 1.0 - (waste / 100.0);
                double netCost = usableFactor > 0 ? price / usableFactor : price;
                netCostLabel.setText(CurrencyFormatter.formatCurrency(netCost) + " / " + unitCombo.getValue().getSymbol());
            } catch (Exception ex) {
                netCostLabel.setText("Error en datos");
            }
        };

        priceField.textProperty().addListener((obs, o, n) -> updateNetCost.run());
        wasteField.textProperty().addListener((obs, o, n) -> updateNetCost.run());
        unitCombo.valueProperty().addListener((obs, o, n) -> updateNetCost.run());

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Categoría:"), 0, 1);
        grid.add(categoryField, 1, 1);
        grid.add(new Label("Precio Compra (€):"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Unidad Medida:"), 0, 3);
        grid.add(unitCombo, 1, 3);
        grid.add(new Label("Merma / Desperdicio (%):"), 0, 4);
        grid.add(wasteField, 1, 4);
        grid.add(new Label("Coste Real Neto Calculado:"), 0, 5);
        grid.add(netCostLabel, 1, 5);
        grid.add(new Label("Proveedor:"), 0, 6);
        grid.add(supplierField, 1, 6);
        grid.add(new Label("Alérgenos:"), 0, 7);
        grid.add(allergensField, 1, 7);

        dialog.getDialogPane().setContent(grid);
        updateNetCost.run();

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnSaveType) {
                try {
                    String name = nameField.getText().trim();
                    if (name.isEmpty()) return null;

                    double price = Double.parseDouble(priceField.getText().replace(",", "."));
                    double waste = Double.parseDouble(wasteField.getText().replace(",", "."));

                    Ingredient ing = isEdit ? ingredientToEdit : new Ingredient();
                    ing.setName(name);
                    ing.setCategory(categoryField.getText().trim());
                    ing.setPurchasePrice(price);
                    ing.setUnit(unitCombo.getValue());
                    ing.setWastePercentage(waste);
                    ing.setSupplier(supplierField.getText().trim());
                    ing.setAllergens(allergensField.getText().trim());
                    return ing;
                } catch (Exception ex) {
                    return null;
                }
            }
            return null;
        });

        Optional<Ingredient> result = dialog.showAndWait();
        result.ifPresent(ing -> {
            repository.save(ing);
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
