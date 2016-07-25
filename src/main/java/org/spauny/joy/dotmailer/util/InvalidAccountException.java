package org.spauny.joy.dotmailer.util;

/**
 *
 * @author iulian
 */
public class InvalidAccountException extends Exception {
    
    public InvalidAccountException(String message) {
        super(message);
    }

    public InvalidAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
