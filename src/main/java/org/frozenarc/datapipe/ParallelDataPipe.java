package org.frozenarc.datapipe;

import org.frozenarc.datapipe.joiner.ParallelStreamJoiner;
import org.frozenarc.datapipe.reader.ParallelStreamReader;
import org.frozenarc.datapipe.writer.ParallelStreamWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: mpanchal
 * Date: 2022-12-24 16:24
 */
public class ParallelDataPipe {

    private ParallelStreamWriter writer;
    private ParallelStreamJoiner[] joiners;
    private ParallelStreamReader reader;

    private ParallelDataPipe(ParallelStreamWriter writer, ParallelStreamJoiner[] joiners, ParallelStreamReader reader) {
        this.writer = writer;
        this.joiners = joiners;
        this.reader = reader;
    }

    public void doStream() {
        int count = writer.getStreamWriterCount();
        for(ParallelStreamJoiner joiner : joiners) {
            count = count + joiner.getReadingJoinerCount() + joiner.getWritingJoinerCount();
        }
        count = count + reader.getStreamReaderCount();


    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ParallelStreamWriter writer;
        private List<ParallelStreamJoiner> joiners = new ArrayList<>();
        private ParallelStreamReader reader;

        public ParallelDataPipe.Builder writer(ParallelStreamWriter writer) {
            this.writer = writer;
            return this;
        }

        public ParallelDataPipe.Builder addJoiner(ParallelStreamJoiner joiner) {
            joiners.add(joiner);
            return this;
        }

        public ParallelDataPipe.Builder reader(ParallelStreamReader reader) {
            this.reader = reader;
            return this;
        }

        public ParallelDataPipe build() {
            return new ParallelDataPipe(writer, joiners.toArray(new ParallelStreamJoiner[]{}), reader);
        }
    }
}
/*
    ParallelDataPipe.builder()
                    .writer(ParallelStreamWriter.streamWriters(outputStream -> {}, outputStream -> {}, outputStream -> {}).build())
                    .joiner(ParallelStreamJoiner.readingJoiners((inputSteam, queue[]) -> {}, (inputSteam, queue[]) -> {}, (inputSteam, queue[]) -> {})
                                                .writingJoiners((queue, outputStream) -> {}, (queue, outputStream) -> {})
                                                .build())
                    .reader(ParallelStreamReader.streamReaders(inputStream -> {},inputStream -> {}).build())
                    .build();

 */