package org.frozenarc.datapipe.joiner;

import org.frozenarc.datapipe.Queue;

import java.io.InputStream;

/**
 * Author: mpanchal
 * Date: 2022-12-24 17:18
 */
public interface ReadingJoiner {

    void readJoin(InputStream inputStream, Queue[] queues);
}
