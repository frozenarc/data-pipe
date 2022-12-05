package org.frozenarc.datapipe.writer;

import org.frozenarc.datapipe.PipeStreamException;

/**
 * Author: mpanchal
 * Date: 2022-12-03 16:03
 * The exception can be thrown during writing stage.
 */
public class WriteException extends PipeStreamException {

    public WriteException(String message) {
        super(message);
    }

    public WriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public WriteException(Throwable cause) {
        super(cause);
    }
}
