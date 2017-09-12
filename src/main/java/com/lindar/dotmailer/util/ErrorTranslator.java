package com.lindar.dotmailer.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ErrorTranslator {

    private static final String ERROR_PROPS = "errors.properties";

    private Properties errorMessages = new Properties();

    private static ErrorTranslator instance;

    private ErrorTranslator(){
        InputStream input = null;

        try {

            input = ErrorTranslator.class.getClassLoader().getResourceAsStream(ERROR_PROPS);
            if(input==null){
                return;
            }

            //load a properties file from class path, inside static method
            errorMessages.load(input);

        } catch (IOException ex) {
            // unable to load error messages
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ErrorTranslator getInstance(){
        if(instance == null){
            instance = new ErrorTranslator();
        }
        return instance;
    }

    public String translate(String errorCode, String defaultError){
        return errorMessages.getProperty(errorCode, defaultError);
    }

    public String translate(String errorCode){
        return translate(errorCode, errorCode);
    }

}
