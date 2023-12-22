package org.frozenarc.datapipe.joiner;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/*
 * Author: mpanchal
 * Date: 19-12-2023
 */
public interface JoinerFuture {

    CompletableFuture<Void> getFuture(StreamJoiner joiner, Consumer<Exception> expConsumer);
}
