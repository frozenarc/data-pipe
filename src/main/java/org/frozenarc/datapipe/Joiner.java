package org.frozenarc.datapipe;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/*
 * Author: mpanchal
 * Date: 02-02-2022
 */
public interface Joiner {

    void join(PipedInputStream inputStream, PipedOutputStream outputStream) throws DataPipeException;
}
