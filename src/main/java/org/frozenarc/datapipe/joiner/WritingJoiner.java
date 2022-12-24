package org.frozenarc.datapipe.joiner;

import org.frozenarc.datapipe.Queue;

import java.io.OutputStream;

/**
 * Author: mpanchal
 * Date: 2022-12-24 17:19
 */
public interface WritingJoiner {

    void writeJoin(Queue queue, OutputStream outputStream);
}
