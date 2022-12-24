package org.frozenarc.datapipe;

import org.frozenarc.datapipe.joiner.ParallelStreamJoiner;
import org.frozenarc.datapipe.reader.ParallelStreamReader;
import org.frozenarc.datapipe.reader.StreamReader;
import org.frozenarc.datapipe.writer.ParallelStreamWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: mpanchal
 * Date: 2022-12-24 16:24
 */
public class ParallelDataPipe {

    private int source;
    private ParallelStreamReader reader;
    private ParallelStreamJoiner[] joiners;
    private ParallelStreamWriter writer;

    private int sink;

    private ParallelDataPipe(int source, ParallelStreamReader reader, ParallelStreamJoiner[] joiners, ParallelStreamWriter writer, int sink) {
        this.source = source;
        this.reader = reader;
        this.joiners = joiners;
        this.writer = writer;
        this.sink = sink;
    }

    public void doStream() {
        writer.
    }

    public static class Builder {
        private StreamReader[] readers;
        private List<ParallelStreamJoiner> joiners = new ArrayList<>();
        private ParallelStreamWriter writer;


        public ParallelDataPipe.Builder reader(StreamReader ... readers) {
            this.readers = readers;
            return this;
        }

        public ParallelDataPipe.Builder addJoiner(ParallelStreamJoiner joiner) {
            joiners.add(joiner);
            return this;
        }

        public ParallelDataPipe.Builder writer(ParallelStreamWriter writer) {
            this.writer = writer;
            return this;
        }

        public ParallelDataPipe build(int source, int sink) {
            return new ParallelDataPipe(source, reader, joiners.toArray(new ParallelStreamJoiner[]{}), writer, sink);
        }
    }
}
