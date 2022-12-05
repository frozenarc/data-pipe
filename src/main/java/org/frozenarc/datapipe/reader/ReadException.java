package org.frozenarc.datapipe.reader;

import org.frozenarc.datapipe.StreamProcessException;

/**
 * Author: mpanchal
 * Date: 2022-12-03 16:03
 * The exception can be thrown during reading stage.
 */
public class ReadException extends StreamProcessException {

    public ReadException(String message) {
        super(message);
    }

    public ReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadException(Throwable cause) {
        super(cause);
    }
}
