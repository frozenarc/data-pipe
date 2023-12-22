package org.frozenarc.datapipe.reader;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/*
 * Author: mpanchal
 * Date: 19-12-2023
 */
public interface ReaderFuture {

    CompletableFuture<Void> getFuture(StreamReader reader, Consumer<Exception> expConsumer);
}
