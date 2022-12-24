package org.frozenarc.datapipe.writer;

/**
 * Author: mpanchal
 * Date: 2022-12-24 17:22
 */
public interface ParallelStreamWriter {

    void write(StreamWriter[] streamWriters);
}
