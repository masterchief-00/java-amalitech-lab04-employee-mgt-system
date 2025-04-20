package com.kwizera.javaamalitechlabemployeemgtsystem.utils;

import javafx.util.StringConverter;

public class SafeDoubleStringConverterUtil extends StringConverter<Double> {

    @Override
    public String toString(Double value) {
        return value != null ? String.format("%.2f", value) : "";
    }

    @Override
    public Double fromString(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
