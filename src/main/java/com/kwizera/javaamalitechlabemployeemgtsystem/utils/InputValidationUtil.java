package com.kwizera.javaamalitechlabemployeemgtsystem.utils;

// defines all methods used for input validation
public class InputValidationUtil {
    // validating names
    public boolean invalidNames(String names) {
        return (!names.matches("[A-Za-z ]*") || names.length() < 2);
    }

    // validating salary input
    public boolean invalidSalary(String salary) {
        try {
            if (salary.isEmpty()) return true;

            double salaryDbl = Double.parseDouble(salary);
            boolean regexMatch = salary.matches("^\\d+(\\.\\d+)?$");
            boolean aboveZero = salaryDbl > 0;
            return !regexMatch || !aboveZero;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    // validating experience input
    public boolean invalidExperienceYears(String exp) {
        try {
            if (exp.isEmpty()) return true;

            double expDbl = Double.parseDouble(exp);
            boolean regexMatch = exp.matches("[0-9]{1,2}");
            boolean notBelowZero = expDbl >= 0;

            return !regexMatch || !notBelowZero;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    // validating for rating input
    public boolean invalidRating(String rate) {
        try {
            if (rate.isEmpty()) return true;

            double rateDbl = Double.parseDouble(rate);
            boolean regexMatch = rate.matches("\\d+(\\.\\d+)?");
            boolean notOutOfRange = rateDbl >= 0 && rateDbl <= 5;

            return !regexMatch || !notOutOfRange;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
