package com.kwizera.javaamalitechlabemployeemgtsystem.utils;

import javafx.util.StringConverter;

public class SafeIntegerStringConverterUtil extends StringConverter<Integer> {
    @Override
    public String toString(Integer value) {
        return value != null ? String.format("%d", value) : "";
    }

    @Override
    public Integer fromString(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
