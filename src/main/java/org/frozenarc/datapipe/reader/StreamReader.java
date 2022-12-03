package org.frozenarc.datapipe.reader;

import java.io.InputStream;

/**
 * Author: mpanchal
 * Date: 2022-12-03 15:34
 */
public interface StreamReader {

    void readFrom(InputStream inputStream) throws ReadException;
}
