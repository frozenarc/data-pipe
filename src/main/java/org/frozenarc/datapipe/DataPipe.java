package org.frozenarc.datapipe;

import org.frozenarc.datapipe.joiner.JoinerComponents;
import org.frozenarc.datapipe.joiner.JoinerFuture;
import org.frozenarc.datapipe.joiner.StreamJoiner;
import org.frozenarc.datapipe.reader.ReaderComponents;
import org.frozenarc.datapipe.reader.ReaderFuture;
import org.frozenarc.datapipe.reader.StreamReader;
import org.frozenarc.datapipe.writer.StreamWriter;
import org.frozenarc.datapipe.writer.WriterComponents;
import org.frozenarc.datapipe.writer.WriterFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Author: mpanchal
 * Date: 2022-12-03 15:28
 * Main class to work with.
 * builder() method to be used to create instance.
 * steamWriter(), addStreamJoiner(), streamReader() to be used to create whole data pipeline to process data from source to sink
 */
@SuppressWarnings("unused")
public class DataPipe {

    private static final Logger log = LoggerFactory.getLogger(DataPipe.class);

    private final WriterComponents writerComps;
    private final JoinerComponents[] joinersComps;
    private final ReaderComponents readerComps;

    private DataPipe(WriterComponents writerComps,
                     JoinerComponents[] joinersComps,
                     ReaderComponents readerComps) {
        this.writerComps = writerComps;
        this.joinersComps = joinersComps;
        this.readerComps = readerComps;
    }

    /**
     * Call the method to start streaming data from source to sink
     *
     * @throws DataPipeException is main exception which will contains main cause as well as suppressed exceptions from other stages
     */
    public void doStream() throws DataPipeException {
        ExecutorService executor = null;
        try {
            executor = Executors.newFixedThreadPool(joinersComps.length + 2);
            doStream(executor);
        } finally {
            if (executor != null) {
                executor.shutdown();
                log.debug("Executor has been shutdown");
            }
        }
    }

    /**
     * Call the method to start streaming data from source to sink with customized executor
     *
     * @param executor ExecutorService
     * @throws DataPipeException is main exception which will contains main cause as well as suppressed exceptions from other stages
     */
    public void doStream(ExecutorService executor) throws DataPipeException {
        List<PipedStream> pipedStreams = new ArrayList<>();
        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());
        try {
            for (int i = 0; i < joinersComps.length + 1; i++) {
                pipedStreams.add(new PipedStream());
            }
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            int i = 0;
            futures.add(writerFuture(pipedStreams.get(i),
                                     executor)
                                .getFuture(writerComps.getWriter(),
                                           Optional.ofNullable(writerComps.getExpConsumer())
                                                   .map(consumer -> getCombinedConsumer(exceptions, consumer))
                                                   .orElse(exceptions::add)));
            while (i < joinersComps.length) {
                futures.add(joinerFuture(pipedStreams.get(i),
                                         pipedStreams.get(i + 1),
                                         executor)
                                    .getFuture(joinersComps[i].getJoiner(),
                                               Optional.ofNullable(joinersComps[i].getExpConsumer())
                                                       .map(consumer -> getCombinedConsumer(exceptions, consumer))
                                                       .orElse(exceptions::add)));
                i++;
            }
            futures.add(readerFuture(pipedStreams.get(i),
                                     executor)
                                .getFuture(readerComps.getReader(),
                                           Optional.ofNullable(readerComps.getExpConsumer())
                                                   .map(consumer -> getCombinedConsumer(exceptions, consumer))
                                                   .orElse(exceptions::add)));

            log.debug("all components are set.. streaming is started");

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{}))
                             .join();

            log.debug("streaming is done");

            if (!exceptions.isEmpty()) {
                DataPipeException exp = new DataPipeException(exceptions.get(0));
                for (int j = 1; j < exceptions.size(); j++) {
                    exp.addSuppressed(exceptions.get(j));
                }
                throw exp;
            }

            log.debug("doStream end");

        } finally {
            for (PipedStream pipedStream : pipedStreams) {
                pipedStream.close();
                log.debug("all piped streams are closed");
            }
        }
    }

    private CompletableFuture<Void> writerFuture(PipedStream pipedStream,
                                                 Executor executor,
                                                 StreamWriter writer,
                                                 Consumer<Exception> expConsumer) {

        return CompletableFuture.runAsync(() -> {
                                              boolean error = false;
                                              try {
                                                  log.debug("StreamWriter: start writing to output stream");
                                                  writer.writeTo(pipedStream.getOutputStream());
                                                  log.debug("StreamWriter: written to output stream");
                                              } catch (Exception ex) {
                                                  error = true;
                                                  expConsumer.accept(ex);
                                                  log.error("StreamWriter: Error during writing to output stream", ex);
                                              } finally {
                                                  pipedStream.closeAfterWrite(error);
                                              }
                                          },
                                          executor);
    }

    private WriterFuture writerFuture(PipedStream pipedStream, Executor executor) {
        return (writer, expConsumer) -> writerFuture(pipedStream, executor, writer, expConsumer);
    }

    private CompletableFuture<Void> readerFuture(PipedStream pipedStream,
                                                 Executor executor,
                                                 StreamReader reader,
                                                 Consumer<Exception> expConsumer) {

        return CompletableFuture.runAsync(() -> {
                                              boolean error = false;
                                              try {
                                                  log.debug("StreamReader: start reading from input stream");
                                                  reader.readFrom(pipedStream.getInputStream());
                                                  log.debug("StreamReader: read from input stream");
                                              } catch (Exception ex) {
                                                  error = true;
                                                  expConsumer.accept(ex);
                                                  log.error("StreamReader: Error during reading from input stream", ex);
                                              } finally {
                                                  pipedStream.closeAfterRead(error);
                                              }
                                          },
                                          executor);
    }

    private ReaderFuture readerFuture(PipedStream pipedStream, Executor executor) {
        return (reader, expConsumer) -> readerFuture(pipedStream, executor, reader, expConsumer);
    }

    private CompletableFuture<Void> joinerFuture(PipedStream inputPipedStream,
                                                 PipedStream outputPipedStream,
                                                 Executor executor,
                                                 StreamJoiner joiner,
                                                 Consumer<Exception> expConsumer) {

        return CompletableFuture.runAsync(() -> {
                                              boolean error = false;
                                              try {
                                                  log.debug("StreamJoiner: start reading from input stream and writing to output stream");
                                                  joiner.join(inputPipedStream.getInputStream(), outputPipedStream.getOutputStream());
                                                  log.debug("StreamJoiner: read from input stream and written to output stream");
                                              } catch (Exception ex) {
                                                  error = true;
                                                  expConsumer.accept(ex);
                                                  log.error("StreamJoiner: Error during joining of input stream and output stream", ex);
                                              } finally {
                                                  inputPipedStream.closeAfterRead(error);
                                                  outputPipedStream.closeAfterWrite(error);
                                              }
                                          },
                                          executor);
    }

    private JoinerFuture joinerFuture(PipedStream inputPipedStream, PipedStream outputPipedStream, Executor executor) {
        return (joiner, expConsumer) -> joinerFuture(inputPipedStream, outputPipedStream, executor, joiner, expConsumer);
    }

    private Consumer<Exception> getCombinedConsumer(List<Exception> exceptions, Consumer<Exception> consumer) {
        return exp -> {
            exceptions.add(exp);
            consumer.accept(exp);
        };
    }

    /**
     * creates builder instance
     *
     * @return DataPipe.Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class
     */
    public static class Builder {
        private WriterComponents writerComps;

        private final List<JoinerComponents> joinerComps = new ArrayList<>();

        private ReaderComponents readerComps;


        /**
         * To be used to set StreamWriter
         *
         * @param writer StreamWriter
         * @return DataPipe.Builder
         */
        public Builder streamWriter(StreamWriter writer) {
            this.writerComps = new WriterComponents(writer, null);
            return this;
        }

        public Builder streamWriter(StreamWriter writer, Consumer<Exception> expConsumer) {
            this.writerComps = new WriterComponents(writer, expConsumer);
            return this;
        }

        /**
         * To be used to add StreamJoiner
         *
         * @param joiner StreamJoiner
         * @return DataPipe.Builder
         */
        public Builder addStreamJoiner(StreamJoiner joiner) {
            this.joinerComps.add(new JoinerComponents(joiner, null));
            return this;
        }

        public Builder addStreamJoiner(StreamJoiner joiner, Consumer<Exception> expConsumer) {
            this.joinerComps.add(new JoinerComponents(joiner, expConsumer));
            return this;
        }

        /**
         * To be used to set StreamReader
         *
         * @param reader StreamReader
         * @return DataPipe.Builder
         */
        public Builder streamReader(StreamReader reader) {
            this.readerComps = new ReaderComponents(reader, null);
            return this;
        }

        public Builder streamReader(StreamReader reader, Consumer<Exception> expConsumer) {
            this.readerComps = new ReaderComponents(reader, expConsumer);
            return this;
        }

        /**
         * builds DataPipe instance
         *
         * @return DataPipe
         */
        public DataPipe build() {
            return new DataPipe(writerComps,
                                joinerComps.toArray(new JoinerComponents[]{}),
                                readerComps);
        }
    }
}
