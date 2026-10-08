package com.escandallos.ui;

import com.escandallos.repository.IngredientRepository;
import com.escandallos.repository.RecipeRepository;
import com.escandallos.ui.views.DashboardView;
import com.escandallos.ui.views.IngredientView;
import com.escandallos.ui.views.RecipeView;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Vista principal de la aplicación JavaFX con navegación por pestañas y barra de estado.
 */
public class MainView extends BorderPane {
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    private final DashboardView dashboardView;
    private final IngredientView ingredientView;
    private final RecipeView recipeView;
    private final Label statusLabel;

    public MainView() {
        this.ingredientRepository = new IngredientRepository();
        this.recipeRepository = new RecipeRepository();

        Runnable globalRefresh = this::refreshAllData;

        dashboardView = new DashboardView(ingredientRepository, recipeRepository);
        ingredientView = new IngredientView(ingredientRepository, globalRefresh);
        recipeView = new RecipeView(recipeRepository, ingredientRepository, globalRefresh);

        // Header Superior
        HBox topBar = new HBox(15);
        topBar.getStyleClass().add("top-bar");

        Label brandLabel = new Label("🍳 GESTOR DE ESCANDALLOS");
        brandLabel.getStyleClass().add("brand-label");

        HBox.setHgrow(brandLabel, Priority.ALWAYS);

        Button btnRefresh = new Button("🔄 Actualizar Datos");
        btnRefresh.setOnAction(e -> refreshAllData());

        topBar.getChildren().addAll(brandLabel, btnRefresh);
        setTop(topBar);

        // TabPane Central
        TabPane tabPane = new TabPane();

        Tab tabDashboard = new Tab("📊 Dashboard & Rentabilidad", dashboardView);
        tabDashboard.setClosable(false);

        Tab tabIngredients = new Tab("🥩 Ingredientes & Mermas", ingredientView);
        tabIngredients.setClosable(false);

        Tab tabRecipes = new Tab("📖 Escandallos & Fichas Técnicas", recipeView);
        tabRecipes.setClosable(false);

        tabPane.getTabs().addAll(tabDashboard, tabIngredients, tabRecipes);
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> refreshAllData());

        setCenter(tabPane);

        // Footer Barra de Estado
        HBox statusBar = new HBox();
        statusBar.getStyleClass().add("status-bar");
        statusLabel = new Label("Estado: Base de Datos SQLite conectada correctamente | Persistencia relacional activa");
        statusLabel.getStyleClass().add("status-text");
        statusBar.getChildren().add(statusLabel);
        setBottom(statusBar);

        refreshAllData();
    }

    public void refreshAllData() {
        dashboardView.refreshData();
        ingredientView.refreshTable();
        recipeView.refreshTable();
        statusLabel.setText("Estado: Datos sincronizados (" +
                ingredientRepository.findAll().size() + " ingredientes, " +
                recipeRepository.findAll().size() + " escandallos activos)");
    }
}
