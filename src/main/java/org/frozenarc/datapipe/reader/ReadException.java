package org.frozenarc.datapipe.reader;

import org.frozenarc.datapipe.PipeStreamException;

/**
 * Author: mpanchal
 * Date: 2022-12-03 16:03
 */
public class ReadException extends PipeStreamException {

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
