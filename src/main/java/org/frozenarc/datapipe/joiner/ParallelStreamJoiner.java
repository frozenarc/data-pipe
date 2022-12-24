package org.frozenarc.datapipe.joiner;

/**
 * Author: mpanchal
 * Date: 2022-12-24 17:20
 */
public interface ParallelStreamJoiner {

    void join(ReadingJoiner[] readingJoiners, WritingJoiner[] writingJoiners);
}
