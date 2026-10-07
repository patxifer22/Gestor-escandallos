package com.escandallos.model;

/**
 * Categorías de platos y escandallos.
 */
public enum Category {
    ENTRANTE("Entrante"),
    PRINCIPAL("Plato Principal"),
    POSTRE("Postre"),
    BEBIDA("Bebida"),
    SALSA("Salsa / Base"),
    GUARNICION("Guarnición"),
    OTRO("Otro");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
