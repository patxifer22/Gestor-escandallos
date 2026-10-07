package com.escandallos.model;

/**
 * Unidades de medida soportadas para ingredientes y escandallos.
 */
public enum Unit {
    KG("Kilogramo", "kg"),
    G("Gramo", "g"),
    L("Litro", "L"),
    ML("Mililitro", "ml"),
    UD("Unidad", "ud");

    private final String displayName;
    private final String symbol;

    Unit(String displayName, String symbol) {
        this.displayName = displayName;
        this.symbol = symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return displayName + " (" + symbol + ")";
    }
}
