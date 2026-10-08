package com.escandallos.repository;

import com.escandallos.model.Ingredient;
import com.escandallos.model.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para Ingredientes mediante JDBC y SQLite.
 */
public class IngredientRepository {

    public List<Ingredient> findAll() {
        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT id, name, category, purchase_price, unit, waste_percentage, supplier, allergens FROM ingredients ORDER BY name COLLATE NOCASE ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar ingredientes: " + e.getMessage());
        }
        return list;
    }

    public Optional<Ingredient> findById(String id) {
        if (id == null) return Optional.empty();
        String sql = "SELECT id, name, category, purchase_price, unit, waste_percentage, supplier, allergens FROM ingredients WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar ingrediente por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Ingredient> searchByName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return findAll();
        }

        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT id, name, category, purchase_price, unit, waste_percentage, supplier, allergens FROM ingredients WHERE name LIKE ? ORDER BY name COLLATE NOCASE ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + query.trim() + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar ingredientes por nombre: " + e.getMessage());
        }
        return list;
    }

    public void save(Ingredient ingredient) {
        if (ingredient == null) return;

        String sql = """
            INSERT INTO ingredients (id, name, category, purchase_price, unit, waste_percentage, supplier, allergens)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT(id) DO UPDATE SET
                name = excluded.name,
                category = excluded.category,
                purchase_price = excluded.purchase_price,
                unit = excluded.unit,
                waste_percentage = excluded.waste_percentage,
                supplier = excluded.supplier,
                allergens = excluded.allergens
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ingredient.getId());
            stmt.setString(2, ingredient.getName());
            stmt.setString(3, ingredient.getCategory());
            stmt.setDouble(4, ingredient.getPurchasePrice());
            stmt.setString(5, ingredient.getUnit() != null ? ingredient.getUnit().name() : Unit.KG.name());
            stmt.setDouble(6, ingredient.getWastePercentage());
            stmt.setString(7, ingredient.getSupplier());
            stmt.setString(8, ingredient.getAllergens());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar ingrediente: " + e.getMessage());
        }
    }

    public void deleteById(String id) {
        if (id == null) return;
        String sql = "DELETE FROM ingredients WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar ingrediente: " + e.getMessage());
        }
    }

    public void saveAll(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) return;

        String sql = """
            INSERT INTO ingredients (id, name, category, purchase_price, unit, waste_percentage, supplier, allergens)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT(id) DO UPDATE SET
                name = excluded.name,
                category = excluded.category,
                purchase_price = excluded.purchase_price,
                unit = excluded.unit,
                waste_percentage = excluded.waste_percentage,
                supplier = excluded.supplier,
                allergens = excluded.allergens
        """;

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (Ingredient ing : ingredients) {
                    stmt.setString(1, ing.getId());
                    stmt.setString(2, ing.getName());
                    stmt.setString(3, ing.getCategory());
                    stmt.setDouble(4, ing.getPurchasePrice());
                    stmt.setString(5, ing.getUnit() != null ? ing.getUnit().name() : Unit.KG.name());
                    stmt.setDouble(6, ing.getWastePercentage());
                    stmt.setString(7, ing.getSupplier());
                    stmt.setString(8, ing.getAllergens());
                    stmt.addBatch();
                }
                stmt.executeBatch();
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error en transacción guardando lista de ingredientes: " + e.getMessage());
        }
    }

    private Ingredient mapResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        String category = rs.getString("category");
        double purchasePrice = rs.getDouble("purchase_price");
        Unit unit = Unit.KG;
        try {
            unit = Unit.valueOf(rs.getString("unit"));
        } catch (Exception ignored) {}

        double wastePercentage = rs.getDouble("waste_percentage");
        String supplier = rs.getString("supplier");
        String allergens = rs.getString("allergens");

        return new Ingredient(id, name, category, purchasePrice, unit, wastePercentage, supplier, allergens);
    }
}
