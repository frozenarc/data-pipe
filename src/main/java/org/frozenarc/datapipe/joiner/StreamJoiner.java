package org.frozenarc.datapipe.joiner;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: mpanchal
 * Date: 2022-12-03 15:33
 * The stage joins inputStream to outputStream.
 * Developer can read from inputStream process the data and write on to the outputStream.
 * Multiple StreamJoiner stages can be existed.
 */
public interface StreamJoiner {

    /**
     * read, process and write data inside the method.
     * @param inputStream InputStream
     * @param outputStream OutputStream
     * @throws JoinException can be thrown during the stage.
     */
    void join(InputStream inputStream, OutputStream outputStream) throws JoinException;
}
