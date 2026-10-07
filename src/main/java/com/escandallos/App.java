package com.escandallos;

import com.escandallos.repository.IngredientRepository;
import com.escandallos.repository.RecipeRepository;
import com.escandallos.ui.MainView;
import com.escandallos.util.DataSeeder;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Punto de entrada principal JavaFX para la aplicación Gestor de Escandallos.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Sembrar datos iniciales si el archivo local está vacío
        IngredientRepository ingRepo = new IngredientRepository();
        RecipeRepository recRepo = new RecipeRepository();
        DataSeeder.seedIfEmpty(ingRepo, recRepo);

        // Crear la vista principal JavaFX
        MainView mainView = new MainView();

        Scene scene = new Scene(mainView, 1280, 800);

        // Cargar hoja de estilos CSS personalizada
        URL cssUrl = getClass().getResource("/css/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("No se pudo cargar la hoja de estilos /css/styles.css");
        }

        primaryStage.setTitle("Gestor de Escandallos y Control de Costes (JavaFX 21)");
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(680);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
