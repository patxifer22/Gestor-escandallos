package com.escandallos.repository;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestor de conexión y esquema relacional para la base de datos SQLite embebida.
 */
public class DatabaseManager {
    private static final String DB_DIR = "data";
    private static final String DB_FILE = "escandallos.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_DIR + "/" + DB_FILE;

    static {
        initDatabase();
    }

    /**
     * Obtiene una conexión activa con SQLite activando las restricciones de integridad referencial.
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    /**
     * Inicializa las tablas relacionales e índices en caso de que no existan.
     */
    public static void initDatabase() {
        File dir = new File(DB_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String createIngredientsTable = """
            CREATE TABLE IF NOT EXISTS ingredients (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                category TEXT,
                purchase_price REAL NOT NULL,
                unit TEXT NOT NULL,
                waste_percentage REAL NOT NULL DEFAULT 0.0,
                supplier TEXT,
                allergens TEXT
            );
        """;

        String createRecipesTable = """
            CREATE TABLE IF NOT EXISTS recipes (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                category TEXT NOT NULL,
                portions INTEGER NOT NULL DEFAULT 1,
                target_margin_percentage REAL NOT NULL DEFAULT 70.0,
                vat_percentage REAL NOT NULL DEFAULT 10.0,
                real_selling_price_with_vat REAL NOT NULL DEFAULT 0.0,
                notes TEXT
            );
        """;

        String createRecipeItemsTable = """
            CREATE TABLE IF NOT EXISTS recipe_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                recipe_id TEXT NOT NULL,
                ingredient_id TEXT NOT NULL,
                quantity REAL NOT NULL,
                unit TEXT NOT NULL,
                FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
                FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE RESTRICT
            );
        """;

        String createIndexes = """
            CREATE INDEX IF NOT EXISTS idx_ingredients_name ON ingredients(name);
            CREATE INDEX IF NOT EXISTS idx_recipes_name ON recipes(name);
            CREATE INDEX IF NOT EXISTS idx_recipe_items_recipe ON recipe_items(recipe_id);
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createIngredientsTable);
            stmt.execute(createRecipesTable);
            stmt.execute(createRecipeItemsTable);
            stmt.execute(createIndexes);
        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos SQLite: " + e.getMessage());
        }
    }
}
