package org.frozenarc.datapipe.joiner;

import org.frozenarc.datapipe.PipeStreamException;

/**
 * Author: mpanchal
 * Date: 2022-12-03 16:02
 */
public class JoinException extends PipeStreamException {

    public JoinException(String message) {
        super(message);
    }

    public JoinException(String message, Throwable cause) {
        super(message, cause);
    }

    public JoinException(Throwable cause) {
        super(cause);
    }
}
