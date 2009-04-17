package edu.indiana.cs.docsim;

public class InvalidConfigFormatException extends Exception {

    public InvalidConfigFormatException(String message) {
        super(message);
    }

    public InvalidConfigFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConfigFormatException(Throwable cause) {
        super(cause);
    }
}
