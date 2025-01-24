package com.ros.lmsdesktopclient.util.validators;

public class PhoneNumberValidator {

    private PhoneNumberValidator(){}

    private static class SingletonHelper{
        private static final PhoneNumberValidator _phoneValidator = new PhoneNumberValidator();
    }

    public static PhoneNumberValidator getInstance(){
        return SingletonHelper._phoneValidator;
    }

    private static final String PHONE_PATTERN = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";

    public boolean hasValidPhoneNumber(String phone){
        return phone != null && phone.matches(PHONE_PATTERN);
    }
}
