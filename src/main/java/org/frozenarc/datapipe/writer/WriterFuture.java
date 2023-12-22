package org.frozenarc.datapipe.writer;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/*
 * Author: mpanchal
 * Date: 19-12-2023
 */
public interface WriterFuture {

    CompletableFuture<Void> getFuture(StreamWriter writer, Consumer<Exception> expConsumer);
}
