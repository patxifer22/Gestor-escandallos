package com.escandallos.repository;

import com.escandallos.model.Category;
import com.escandallos.model.Recipe;
import com.escandallos.util.JsonStorageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio para la gestión y almacenamiento de escandallos en archivo JSON.
 */
public class RecipeRepository {
    private static final String FILE_PATH = "data/recipes.json";
    private final List<Recipe> recipes;

    public RecipeRepository() {
        this.recipes = JsonStorageUtil.loadList(FILE_PATH, Recipe.class);
    }

    public List<Recipe> findAll() {
        return new ArrayList<>(recipes);
    }

    public Optional<Recipe> findById(String id) {
        return recipes.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    public List<Recipe> findByCategory(Category category) {
        if (category == null) return findAll();
        return recipes.stream()
                .filter(r -> r.getCategory() == category)
                .collect(Collectors.toList());
    }

    public List<Recipe> searchByName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return findAll();
        }
        String lowerQuery = query.toLowerCase().trim();
        return recipes.stream()
                .filter(r -> r.getName() != null && r.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public void save(Recipe recipe) {
        if (recipe == null) return;

        Optional<Recipe> existing = findById(recipe.getId());
        if (existing.isPresent()) {
            int index = recipes.indexOf(existing.get());
            recipes.set(index, recipe);
        } else {
            recipes.add(recipe);
        }
        saveToFile();
    }

    public void deleteById(String id) {
        recipes.removeIf(r -> r.getId().equals(id));
        saveToFile();
    }

    public void saveAll(List<Recipe> newRecipes) {
        this.recipes.clear();
        this.recipes.addAll(newRecipes);
        saveToFile();
    }

    private void saveToFile() {
        JsonStorageUtil.saveList(FILE_PATH, recipes);
    }
}
