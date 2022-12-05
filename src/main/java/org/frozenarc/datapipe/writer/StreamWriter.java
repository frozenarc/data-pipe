package org.frozenarc.datapipe.writer;

import java.io.OutputStream;

/**
 * Author: mpanchal
 * Date: 2022-12-03 15:33
 * This is first stage. This is to be used to write data from source to initial outputStream.
 */
public interface StreamWriter {

    /**
     * The stage writes data to outputStream from source.
     * @param outputStream OutputStream
     * @throws WriteException can be thrown during the stage.
     */
    void writeTo(OutputStream outputStream) throws WriteException;
}
