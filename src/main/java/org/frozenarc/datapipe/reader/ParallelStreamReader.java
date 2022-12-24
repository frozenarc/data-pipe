package org.frozenarc.datapipe.reader;

/**
 * Author: mpanchal
 * Date: 2022-12-24 17:21
 */
public interface ParallelStreamReader {

    void read(StreamReader[] streamReaders);
}
