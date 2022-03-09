package org.frozenarc.datapipe;

import java.io.PipedOutputStream;

/*
 * Author: mpanchal
 * Date: 02-02-2022
 */
public interface SourceWriter {

    void write(PipedOutputStream outputStream) throws DataPipeException;

}
