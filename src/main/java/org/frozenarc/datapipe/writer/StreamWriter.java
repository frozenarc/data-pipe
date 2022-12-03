package org.frozenarc.datapipe.writer;

import java.io.OutputStream;

/**
 * Author: mpanchal
 * Date: 2022-12-03 15:33
 */
public interface StreamWriter {

    void writeTo(OutputStream outputStream) throws WriteException;
}
