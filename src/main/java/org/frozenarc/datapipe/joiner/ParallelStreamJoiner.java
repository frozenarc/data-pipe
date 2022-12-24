package org.frozenarc.datapipe.joiner;

/**
 * Author: mpanchal
 * Date: 2022-12-24 17:20
 */
public class ParallelStreamJoiner {

    private ReadingJoiner[] readingJoiners;
    private WritingJoiner[] writingJoiners;

    private ParallelStreamJoiner(ReadingJoiner[] readingJoiners, WritingJoiner[] writingJoiners) {
        this.readingJoiners = readingJoiners;
        this.writingJoiners = writingJoiners;
    }

    public int getReadingJoinerCount() {
        return readingJoiners.length;
    }

    public int getWritingJoinerCount() {
        return writingJoiners.length;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ReadingJoiner[] readingJoiners;
        private WritingJoiner[] writingJoiners;


        public ParallelStreamJoiner.Builder readingJoiners(ReadingJoiner ... readingJoiners) {
            this.readingJoiners = readingJoiners;
            return this;
        }

        public ParallelStreamJoiner.Builder writingJoiners(WritingJoiner ... writingJoiners) {
            this.writingJoiners = writingJoiners;
            return this;
        }

        public ParallelStreamJoiner build() {
            return new ParallelStreamJoiner(readingJoiners, writingJoiners);
        }
    }


}
