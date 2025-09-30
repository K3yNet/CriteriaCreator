package com.example.criteria_project.filter;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class H2Functions {

    /**
     * Remove acentos de uma string. Esta função será exposta ao H2 como 'UNACCENT'.
     * @param value A string original com acentos.
     * @return A string normalizada sem acentos.
     */
    public static String removeAccents(String value) {
        if (value == null) {
            return null;
        }
        String normalizer = Normalizer.normalize(value, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizer).replaceAll("");
    }
}