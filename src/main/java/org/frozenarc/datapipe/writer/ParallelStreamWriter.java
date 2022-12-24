package org.frozenarc.datapipe.writer;

/**
 * Author: mpanchal
 * Date: 2022-12-24 17:22
 */
public class ParallelStreamWriter {

    private StreamWriter[] streamWriters;

    private ParallelStreamWriter(StreamWriter[] streamWriters) {
        this.streamWriters = streamWriters;
    }

    public int getStreamWriterCount() {
        return this.streamWriters.length;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private StreamWriter[] streamWriters;

        public ParallelStreamWriter.Builder streamWriters(StreamWriter ... streamWriters) {
            this.streamWriters = streamWriters;
            return this;
        }

        public ParallelStreamWriter build() {
            return new ParallelStreamWriter(streamWriters);
        }
    }
}
