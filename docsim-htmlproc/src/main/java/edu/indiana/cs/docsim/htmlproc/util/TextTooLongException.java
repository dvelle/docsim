package edu.indiana.cs.docsim.htmlproc.util;

/**
 * Represents that a text is too long so that it exceeds some defined limit.
 *
 * @author
 * @version
 */
public class TextTooLongException
  extends Exception {
    public TextTooLongException(String message) {
        super(message);
    }
    public TextTooLongException(String message, Throwable cause) {
        super(message, cause);
    }
    public TextTooLongException(Throwable cause) {
        super(cause);
    }
}
