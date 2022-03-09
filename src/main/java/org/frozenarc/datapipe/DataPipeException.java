package org.frozenarc.datapipe;

/*
 * Author: mpanchal
 * Date: 09-03-2022
 */
@SuppressWarnings("unused")
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
