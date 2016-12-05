package com.smarthome.data;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VerifyAction {
    public boolean hasFourChar(String rUserName) {
        return rUserName.length() > 3;
    }

    public boolean hasEightChar(String rPassword) {
        return rPassword.length() > 7;
    }

    public boolean validEmail(String rEmail) {
        String emailRegRule = "^(.+)@(.+)$";
        Pattern emailPattern = Pattern.compile(emailRegRule);
        Matcher emailMatcher = emailPattern.matcher(rEmail);
        return ((!rEmail.isEmpty()) && (emailMatcher.matches()));
    }
}
