/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Revaldi
 */
public class Formatter {

    private static final NumberFormat RUPIAH_FORMAT = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public static String formatRupiah(double amount) {
        return "Rp" + String.format(new Locale("id", "ID"), "%,d", (long) amount).replace(',', '.');
    }

    public static int parseIntSafe(String text) {
        if (isNullOrEmpty(text)) return 0;
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static double parseDoubleSafe(String text) {
        if (isNullOrEmpty(text)) return 0.0;
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static double parseCurrencySafe(String text) {
        if (isNullOrEmpty(text)) return 0.0;
        String clean = text.replaceAll("[^\\d]", "");
        if (clean.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(clean);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
}
