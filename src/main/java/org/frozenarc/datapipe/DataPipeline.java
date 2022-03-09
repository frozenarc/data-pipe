package org.frozenarc.datapipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Author: mpanchal
 * Date: 02-02-2022
 */
@SuppressWarnings("unused")
public class DataPipeline {

    private static final Logger log = LoggerFactory.getLogger(DataPipeline.class);

    private final SourceWriter writer;
    private final Joiner[] joiners;
    private final SinkReader reader;

    private DataPipeline(SourceWriter writer, Joiner[] joiners, SinkReader reader) {
        this.writer = writer;
        this.joiners = joiners;
        this.reader = reader;
    }

    public static class Util {
        public static void stream(SourceWriter writer, SinkReader reader) throws DataPipeException {
            try {
                List<Throwable> exceptions = new ArrayList<>();
                PipedInputStream inputStream = new PipedInputStream();
                PipedOutputStream outputStream = new PipedOutputStream(inputStream);
                ExecutorService executor = null;
                try {
                    executor = Executors.newFixedThreadPool(2);
                    List<CompletableFuture<Void>> futures
                            = Arrays.asList(CompletableFuture.runAsync(() -> {
                                                boolean error = false;
                                                try {
                                                    log.debug("OutputStream is started being written with source data");
                                                    writer.write(outputStream);
                                                    log.debug("OutputStream is written with source data");
                                                } catch (Exception ex) {
                                                    error = true;
                                                    throw new CompletionException(ex);
                                                } finally {
                                                    try {
                                                        outputStream.close();
                                                        if (error) {
                                                            inputStream.close();
                                                        }
                                                    } catch (IOException ex) {
                                                        log.error("Error closing OutputStream", ex);
                                                    }
                                                }
                                            }, executor),
                                            CompletableFuture.runAsync(() -> {
                                                boolean error = false;
                                                try {
                                                    log.debug("InputStream is started being read for sink data");
                                                    reader.read(inputStream);
                                                    log.debug("InputStream is read for sink data");
                                                } catch (Exception ex) {
                                                    error = true;
                                                    throw new CompletionException(ex);
                                                } finally {
                                                    try {
                                                        if (error) {
                                                            outputStream.close();
                                                        }
                                                        inputStream.close();
                                                    } catch (IOException ex) {
                                                        log.error("Error closing InputStream", ex);
                                                    }
                                                }
                                            }, executor));

                    log.debug("About to collect all futures and join");
                    for (CompletableFuture<Void> future : futures) {
                        future.exceptionally(ex -> {
                            exceptions.add(ex);
                            return null;
                        }).join();
                    }

                    for (Throwable ex : exceptions) {
                        Throwable mainCause = getMainClause(ex);
                        if (mainCause instanceof DataPipeException) {
                            throw new DataPipeException(ex);
                        }
                    }

                } finally {
                    if (executor != null) {
                        executor.shutdown();
                    }
                    log.debug("shutdown executor");
                    outputStream.close();
                    inputStream.close();
                    log.debug("Safe close for both streams");
                }
            } catch (IOException ex) {
                throw new DataPipeException(ex);
            }
        }

        private static Throwable getMainClause(Throwable ex) {
            if (ex.getCause() == null) {
                return ex;
            } else {
                return getMainClause(ex.getCause());
            }
        }

        public static SourceWriter join(SourceWriter writer, Joiner joiner) {
            return outputStream -> stream(writer, inputStream -> joiner.join(inputStream, outputStream));
        }
    }

    public void stream() throws DataPipeException {
        SourceWriter finalWriter = writer;
        for (Joiner joiner : joiners) {
            finalWriter = Util.join(finalWriter, joiner);
        }
        Util.stream(finalWriter, reader);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private SourceWriter writer;
        private Joiner[] joiners = new Joiner[]{};
        private SinkReader reader;

        public Builder sourceWriter(SourceWriter writer) {
            this.writer = writer;
            return this;
        }

        public Builder joiners(Joiner... joiners) {
            this.joiners = joiners;
            return this;
        }

        public Builder sinkReader(SinkReader reader) {
            this.reader = reader;
            return this;
        }

        public DataPipeline build() {
            return new DataPipeline(writer, joiners, reader);
        }
    }
}
