package org.frozenarc.datapipe;

/**
 * Author: mpanchal
 * Date: 2022-12-03 16:17
 */
public class PipeStreamException extends Exception {

    public PipeStreamException(String message) {
        super(message);
    }

    public PipeStreamException(String message, Throwable cause) {
        super(message, cause);
    }

    public PipeStreamException(Throwable cause) {
        super(cause);
    }
}
