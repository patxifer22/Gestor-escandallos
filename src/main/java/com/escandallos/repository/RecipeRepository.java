package com.escandallos.repository;

import com.escandallos.model.Category;
import com.escandallos.model.Ingredient;
import com.escandallos.model.Recipe;
import com.escandallos.model.RecipeItem;
import com.escandallos.model.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para Escandallos (Recetas) mediante JDBC y SQLite.
 * Maneja transacciones relacionales con la tabla de ítems de recetas.
 */
public class RecipeRepository {

    public List<Recipe> findAll() {
        List<Recipe> list = new ArrayList<>();
        String sql = "SELECT id, name, category, portions, target_margin_percentage, vat_percentage, real_selling_price_with_vat, notes FROM recipes ORDER BY name COLLATE NOCASE ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Recipe recipe = mapRecipeSummary(rs);
                loadRecipeItems(conn, recipe);
                list.add(recipe);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar escandallos: " + e.getMessage());
        }
        return list;
    }

    public Optional<Recipe> findById(String id) {
        if (id == null) return Optional.empty();
        String sql = "SELECT id, name, category, portions, target_margin_percentage, vat_percentage, real_selling_price_with_vat, notes FROM recipes WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Recipe recipe = mapRecipeSummary(rs);
                    loadRecipeItems(conn, recipe);
                    return Optional.of(recipe);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar escandallo por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Recipe> findByCategory(Category category) {
        if (category == null) return findAll();

        List<Recipe> list = new ArrayList<>();
        String sql = "SELECT id, name, category, portions, target_margin_percentage, vat_percentage, real_selling_price_with_vat, notes FROM recipes WHERE category = ? ORDER BY name COLLATE NOCASE ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Recipe recipe = mapRecipeSummary(rs);
                    loadRecipeItems(conn, recipe);
                    list.add(recipe);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar escandallos por categoría: " + e.getMessage());
        }
        return list;
    }

    public List<Recipe> searchByName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return findAll();
        }

        List<Recipe> list = new ArrayList<>();
        String sql = "SELECT id, name, category, portions, target_margin_percentage, vat_percentage, real_selling_price_with_vat, notes FROM recipes WHERE name LIKE ? ORDER BY name COLLATE NOCASE ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + query.trim() + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Recipe recipe = mapRecipeSummary(rs);
                    loadRecipeItems(conn, recipe);
                    list.add(recipe);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar escandallos por nombre: " + e.getMessage());
        }
        return list;
    }

    public void save(Recipe recipe) {
        if (recipe == null) return;

        String upsertRecipeSql = """
            INSERT INTO recipes (id, name, category, portions, target_margin_percentage, vat_percentage, real_selling_price_with_vat, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT(id) DO UPDATE SET
                name = excluded.name,
                category = excluded.category,
                portions = excluded.portions,
                target_margin_percentage = excluded.target_margin_percentage,
                vat_percentage = excluded.vat_percentage,
                real_selling_price_with_vat = excluded.real_selling_price_with_vat,
                notes = excluded.notes
        """;

        String deleteItemsSql = "DELETE FROM recipe_items WHERE recipe_id = ?";

        String insertItemSql = """
            INSERT INTO recipe_items (recipe_id, ingredient_id, quantity, unit)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Guardar o actualizar cabecera del escandallo
                try (PreparedStatement stmt = conn.prepareStatement(upsertRecipeSql)) {
                    stmt.setString(1, recipe.getId());
                    stmt.setString(2, recipe.getName());
                    stmt.setString(3, recipe.getCategory() != null ? recipe.getCategory().name() : Category.PRINCIPAL.name());
                    stmt.setInt(4, recipe.getPortions());
                    stmt.setDouble(5, recipe.getTargetMarginPercentage());
                    stmt.setDouble(6, recipe.getVatPercentage());
                    stmt.setDouble(7, recipe.getRealSellingPriceWithVat());
                    stmt.setString(8, recipe.getNotes());
                    stmt.executeUpdate();
                }

                // 2. Eliminar líneas anteriores
                try (PreparedStatement stmt = conn.prepareStatement(deleteItemsSql)) {
                    stmt.setString(1, recipe.getId());
                    stmt.executeUpdate();
                }

                // 3. Insertar nuevas líneas de ingredientes
                if (recipe.getItems() != null && !recipe.getItems().isEmpty()) {
                    try (PreparedStatement stmt = conn.prepareStatement(insertItemSql)) {
                        for (RecipeItem item : recipe.getItems()) {
                            if (item.getIngredient() != null) {
                                stmt.setString(1, recipe.getId());
                                stmt.setString(2, item.getIngredient().getId());
                                stmt.setDouble(3, item.getQuantity());
                                stmt.setString(4, item.getUnit() != null ? item.getUnit().name() : Unit.KG.name());
                                stmt.addBatch();
                            }
                        }
                        stmt.executeBatch();
                    }
                }

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar escandallo en base de datos: " + e.getMessage());
        }
    }

    public void deleteById(String id) {
        if (id == null) return;
        String sql = "DELETE FROM recipes WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar escandallo: " + e.getMessage());
        }
    }

    private Recipe mapRecipeSummary(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setId(rs.getString("id"));
        recipe.setName(rs.getString("name"));
        try {
            recipe.setCategory(Category.valueOf(rs.getString("category")));
        } catch (Exception e) {
            recipe.setCategory(Category.PRINCIPAL);
        }
        recipe.setPortions(rs.getInt("portions"));
        recipe.setTargetMarginPercentage(rs.getDouble("target_margin_percentage"));
        recipe.setVatPercentage(rs.getDouble("vat_percentage"));
        recipe.setRealSellingPriceWithVat(rs.getDouble("real_selling_price_with_vat"));
        recipe.setNotes(rs.getString("notes"));
        return recipe;
    }

    private void loadRecipeItems(Connection conn, Recipe recipe) throws SQLException {
        String sql = """
            SELECT ri.quantity, ri.unit AS item_unit,
                   i.id AS ing_id, i.name AS ing_name, i.category AS ing_category,
                   i.purchase_price, i.unit AS ing_unit, i.waste_percentage, i.supplier, i.allergens
            FROM recipe_items ri
            JOIN ingredients i ON ri.ingredient_id = i.id
            WHERE ri.recipe_id = ?
        """;

        List<RecipeItem> items = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipe.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Unit ingUnit = Unit.KG;
                    try {
                        ingUnit = Unit.valueOf(rs.getString("ing_unit"));
                    } catch (Exception ignored) {}

                    Ingredient ingredient = new Ingredient(
                            rs.getString("ing_id"),
                            rs.getString("ing_name"),
                            rs.getString("ing_category"),
                            rs.getDouble("purchase_price"),
                            ingUnit,
                            rs.getDouble("waste_percentage"),
                            rs.getString("supplier"),
                            rs.getString("allergens")
                    );

                    Unit itemUnit = ingUnit;
                    try {
                        itemUnit = Unit.valueOf(rs.getString("item_unit"));
                    } catch (Exception ignored) {}

                    double quantity = rs.getDouble("quantity");
                    items.add(new RecipeItem(ingredient, quantity, itemUnit));
                }
            }
        }
        recipe.setItems(items);
    }
}
