package com.project.nicky.medicalmanager.Startup;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static final String LETTERS = "^[a-zA-Z ]+$";
    public static final String NUMBERS = "^[0-9 ]+$";
    public static final String LETTERS_AND_NUMBERS = "^[a-zA-Z0-9 ]+$";
    public int counter = 0;

    public void validate(String type, String value, EditText field, String noInputMessage, String invalidInputMessage) {
        String regExp = type;
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(value);
        if(value.length() < 1){
            field.setError(noInputMessage);
            field.requestFocus();
            counter++;
        }
        else if(!matcher.matches()){
            field.setError(invalidInputMessage);
            field.requestFocus();
            counter++;
        }
    }

    public void checkIfEmpty(String value, EditText field, String noInputMessage){
        if(value.length() < 1){
            field.setError(noInputMessage);
            field.requestFocus();
            counter++;
        }
    }

    public void length(String value, EditText field, String invalidLengthMessage, int min, int max){
        if(value.length() < min || value.length() > max){
            field.setError(invalidLengthMessage);
            field.requestFocus();
            counter++;
        }
    }
}
