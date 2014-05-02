package org.mkdev.collections.exceptions;

/**
 * Created by mkolodziejski on 25.04.2014.
 */
public class FieldCompareFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FieldCompareFailedException() {
        super();
    }

    public FieldCompareFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldCompareFailedException(String message) {
        super(message);
    }

    public FieldCompareFailedException(Throwable cause) {
        super(cause);
    }
}

