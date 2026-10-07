package com.escandallos.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utilidades de formato de moneda (€) y porcentajes (%).
 */
public class CurrencyFormatter {
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
    private static final NumberFormat percentFormat = NumberFormat.getNumberInstance(Locale.GERMANY);

    static {
        percentFormat.setMinimumFractionDigits(1);
        percentFormat.setMaximumFractionDigits(2);
    }

    public static String formatCurrency(double amount) {
        return currencyFormat.format(amount);
    }

    public static String formatPercent(double percent) {
        return percentFormat.format(percent) + " %";
    }

    public static String formatNumber(double number, int decimals) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
        nf.setMinimumFractionDigits(decimals);
        nf.setMaximumFractionDigits(decimals);
        return nf.format(number);
    }
}
