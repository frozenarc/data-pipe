package org.frozenarc.datapipe;

import org.frozenarc.datapipe.joiner.ParallelStreamJoiner;
import org.frozenarc.datapipe.reader.ParallelStreamReader;
import org.frozenarc.datapipe.writer.ParallelStreamWriter;
import org.junit.Test;

/**
 * Author: mpanchal
 * Date: 2022-12-24 19:57
 */
public class ParallelDataPipeTest {

    @Test
    public void test() {
        ParallelDataPipe.builder()
                        .writer(ParallelStreamWriter.builder()
                                                    .streamWriters(outputStream -> {

                                                                   },
                                                                   outputStream -> {

                                                                   },
                                                                   outputStream -> {

                                                                   })
                                                    .build())
                        .addJoiner(ParallelStreamJoiner.builder()
                                                       .readingJoiners((inputStream, queues) -> {

                                                                       },
                                                                       (inputStream, queues) -> {

                                                                       },
                                                                       (inputStream, queues) -> {

                                                                       })
                                                       .writingJoiners((queue, outputStream) -> {

                                                                       },
                                                                       (queue, outputStream) -> {

                                                                       })
                                                       .build())
                        .reader(ParallelStreamReader.builder()
                                                    .streamReaders(inputStream -> {

                                                                   },
                                                                   inputStream -> {

                                                                   })
                                                    .build())
                        .build();
    }
}
