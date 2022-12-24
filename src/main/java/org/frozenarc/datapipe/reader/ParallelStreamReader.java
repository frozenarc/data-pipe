package org.frozenarc.datapipe.reader;

import org.frozenarc.datapipe.joiner.ParallelStreamJoiner;

/**
 * Author: mpanchal
 * Date: 2022-12-24 17:21
 */
public class ParallelStreamReader {

    private StreamReader[] streamReaders;

    private ParallelStreamReader(StreamReader[] streamReaders) {
        this.streamReaders = streamReaders;
    }

    public int getStreamReaderCount() {
        return this.streamReaders.length;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private StreamReader[] streamReaders;


        public ParallelStreamReader.Builder streamReaders(StreamReader ... streamReaders) {
            this.streamReaders = streamReaders;
            return this;
        }

        public ParallelStreamReader build() {
            return new ParallelStreamReader(streamReaders);
        }
    }
}
