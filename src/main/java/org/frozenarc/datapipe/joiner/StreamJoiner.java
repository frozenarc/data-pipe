package org.frozenarc.datapipe.joiner;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: mpanchal
 * Date: 2022-12-03 15:33
 */
public interface StreamJoiner {

    void join(InputStream inputStream, OutputStream outputStream) throws JoinException;
}
