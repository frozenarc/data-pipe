package org.frozenarc.datapipe.reader;

import java.io.InputStream;

/**
 * Author: mpanchal
 * Date: 2022-12-03 15:34
 * This is last stage. This is to be used to read from final inputStream and data can be sent to sink.
 */
public interface StreamReader {

    /**
     * The stage reads from inputStream and send data to sink.
     * @param inputStream InputStream
     * @throws ReadException can be thrown during the stage.
     */
    void readFrom(InputStream inputStream) throws ReadException;
}
