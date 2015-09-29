package com.farmers.underground.ui.utils;

/**
 * Created by samson on 29.09.15.
 */
public class ValidationUtil {

    public static boolean isValidEmail(String emailString) {
        String emailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        return emailString.matches(emailPattern) && emailString.length() > 0 && emailString.length() < 256;
    }

    public static boolean isValidPassword(String passwordString) {
        String passwordPattern = "[A-Z0-9a-z]+";
        return passwordString.matches(passwordPattern) && passwordString.length() > 5 && passwordString.length() < 36;
    }

}
