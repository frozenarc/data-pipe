package org.frozenarc.datapipe;

import java.io.PipedInputStream;

/*
 * Author: mpanchal
 * Date: 02-02-2022
 */
public interface SinkReader {

    void read(PipedInputStream inputStream) throws DataPipeException;
}
