package com.escandallos.repository;

import com.escandallos.model.Ingredient;
import com.escandallos.util.JsonStorageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio para la gestión y almacenamiento persistente de ingredientes en archivo JSON.
 */
public class IngredientRepository {
    private static final String FILE_PATH = "data/ingredients.json";
    private final List<Ingredient> ingredients;

    public IngredientRepository() {
        this.ingredients = JsonStorageUtil.loadList(FILE_PATH, Ingredient.class);
    }

    public List<Ingredient> findAll() {
        return new ArrayList<>(ingredients);
    }

    public Optional<Ingredient> findById(String id) {
        return ingredients.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }

    public List<Ingredient> searchByName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return findAll();
        }
        String lowerQuery = query.toLowerCase().trim();
        return ingredients.stream()
                .filter(i -> i.getName() != null && i.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public void save(Ingredient ingredient) {
        if (ingredient == null) return;
        
        Optional<Ingredient> existing = findById(ingredient.getId());
        if (existing.isPresent()) {
            int index = ingredients.indexOf(existing.get());
            ingredients.set(index, ingredient);
        } else {
            ingredients.add(ingredient);
        }
        saveToFile();
    }

    public void deleteById(String id) {
        ingredients.removeIf(i -> i.getId().equals(id));
        saveToFile();
    }

    public void saveAll(List<Ingredient> newIngredients) {
        this.ingredients.clear();
        this.ingredients.addAll(newIngredients);
        saveToFile();
    }

    private void saveToFile() {
        JsonStorageUtil.saveList(FILE_PATH, ingredients);
    }
}
