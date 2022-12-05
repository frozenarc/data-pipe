package org.frozenarc.datapipe;

/**
 * Author: mpanchal
 * Date: 2022-12-03 16:17
 * Base exception for any individual exception from writer, joiner and reader stage.
 */
public class StreamProcessException extends Exception {

    public StreamProcessException(String message) {
        super(message);
    }

    public StreamProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public StreamProcessException(Throwable cause) {
        super(cause);
    }
}
