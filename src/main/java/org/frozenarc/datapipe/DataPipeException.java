package org.frozenarc.datapipe;

/**
 * Author: mpanchal
 * Date: 2022-12-03 15:32
 * The exception is main exception which will contains main cause as well as suppressed exceptions from other stages
 */
public class DataPipeException extends Exception {

    public DataPipeException(String message) {
        super(message);
    }

    public DataPipeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataPipeException(Throwable cause) {
        super(cause);
    }
}
