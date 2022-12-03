package org.frozenarc.datapipe;

import org.frozenarc.datapipe.joiner.StreamJoiner;
import org.frozenarc.datapipe.reader.StreamReader;
import org.frozenarc.datapipe.writer.StreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Author: mpanchal
 * Date: 2022-12-03 15:28
 */
public class DataPipe {

    private static final Logger log = LoggerFactory.getLogger(DataPipe.class);

    private final StreamWriter writer;
    private final StreamJoiner[] joiners;
    private final StreamReader reader;

    private DataPipe(StreamWriter writer, StreamJoiner[] joiners, StreamReader reader) {
        this.writer = writer;
        this.joiners = joiners;
        this.reader = reader;
    }

    public void doStream() throws DataPipeException {
        doStream(Executors.newFixedThreadPool(joiners.length + 2));
    }

    public void doStream(ExecutorService executor) throws DataPipeException {
        List<PipedStream> pipedStreams = new ArrayList<>();
        List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<>());
        try {
            for (int i = 0; i < joiners.length + 1; i++) {
                pipedStreams.add(new PipedStream());
            }
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            int i = 0;
            futures.add(writerFuture(writer, pipedStreams.get(i), executor, exceptions::add));
            while (i < joiners.length) {
                futures.add(joinerFuture(joiners[i],
                                         pipedStreams.get(i),
                                         pipedStreams.get(i + 1),
                                         executor,
                                         exceptions::add));
                i++;
            }
            futures.add(readerFuture(reader, pipedStreams.get(i), executor, exceptions::add));

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{}))
                             .join();

            if (exceptions.size() > 0) {
                DataPipeException exp = new DataPipeException(exceptions.get(0));
                for (int j = 1; j < exceptions.size(); j++) {
                    exp.addSuppressed(exceptions.get(j));
                }
                throw exp;
            }

        } finally {
            try {
                for (PipedStream pipedStream : pipedStreams) {
                    pipedStream.close();
                }
            } finally {
                executor.shutdown();
            }
        }
    }

    public CompletableFuture<Void> writerFuture(StreamWriter writer,
                                                PipedStream pipedStream,
                                                Executor executor,
                                                Consumer<Throwable> expConsumer) {

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
                                                  try {
                                                      pipedStream.getOutputStream().close();
                                                      if (error) {
                                                          pipedStream.getInputStream().close();
                                                      }
                                                  } catch (IOException ex) {
                                                      ex.printStackTrace();
                                                  }
                                              }
                                          },
                                          executor);
    }

    public CompletableFuture<Void> readerFuture(StreamReader reader,
                                                PipedStream pipedStream,
                                                Executor executor,
                                                Consumer<Throwable> expConsumer) {

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
                                                  try {
                                                      if (error) {
                                                          pipedStream.getOutputStream().close();
                                                      }
                                                      pipedStream.getInputStream().close();
                                                  } catch (IOException ex) {
                                                      ex.printStackTrace();
                                                  }
                                              }
                                          },
                                          executor);
    }

    public CompletableFuture<Void> joinerFuture(StreamJoiner joiner,
                                                PipedStream inputPipedStream,
                                                PipedStream outputPipedStream,
                                                Executor executor,
                                                Consumer<Throwable> expConsumer) {

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
                                                  try {
                                                      if (error) {
                                                          inputPipedStream.getOutputStream().close();
                                                      }
                                                      inputPipedStream.getInputStream().close();
                                                      outputPipedStream.getOutputStream().close();
                                                      if (error) {
                                                          outputPipedStream.getInputStream().close();
                                                      }
                                                  } catch (IOException ex) {
                                                      ex.printStackTrace();
                                                  }
                                              }
                                          },
                                          executor);
    }

    /*private Throwable getCause(Throwable ex) {
        if (ex instanceof DataPipeException && ex.getCause() != null) {
            return getCause(ex.getCause());
        } else {
            return ex;
        }
    }*/

    public static DataPipe.Builder builder() {
        return new DataPipe.Builder();
    }

    public static class Builder {
        private StreamWriter writer;

        private final List<StreamJoiner> joiners = new ArrayList<>();
        private StreamReader reader;

        public DataPipe.Builder streamWriter(StreamWriter writer) {
            this.writer = writer;
            return this;
        }

        public DataPipe.Builder addStreamJoiner(StreamJoiner joiner) {
            joiners.add(joiner);
            return this;
        }

        public DataPipe.Builder streamReader(StreamReader reader) {
            this.reader = reader;
            return this;
        }

        public DataPipe build() {
            return new DataPipe(writer, joiners.toArray(new StreamJoiner[]{}), reader);
        }
    }
}
