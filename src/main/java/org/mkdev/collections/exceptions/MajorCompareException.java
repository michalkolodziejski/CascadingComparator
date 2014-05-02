package org.mkdev.collections.exceptions;

/**
 * Created by mkolodziejski on 25.04.2014.
 */
public class MajorCompareException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MajorCompareException() {
        super();
    }

    public MajorCompareException(String message, Throwable cause) {
        super(message, cause);
    }

    public MajorCompareException(String message) {
        super(message);
    }

    public MajorCompareException(Throwable cause) {
        super(cause);
    }
}

