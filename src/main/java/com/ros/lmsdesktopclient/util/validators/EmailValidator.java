package com.ros.lmsdesktopclient.util.validators;

import com.ros.lmsdesktopclient.util.ViewHandler;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class EmailValidator {

    private EmailValidator(){}

    private static class SingletonHelper{
        private static final EmailValidator _emailValidator = new EmailValidator();
    }

    public static EmailValidator getInstance(){
        return SingletonHelper._emailValidator;
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public boolean hasValidEmail(String email){
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}
