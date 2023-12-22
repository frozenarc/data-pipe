package org.frozenarc.datapipe.reader;

import java.util.function.Consumer;

/*
 * Author: mpanchal
 * Date: 19-12-2023
 */
public class ReaderComponents {

    private final StreamReader reader;

    private final Consumer<Exception> expConsumer;

    public ReaderComponents(StreamReader reader, Consumer<Exception> expConsumer) {
        this.reader = reader;
        this.expConsumer = expConsumer;
    }

    public StreamReader getReader() {
        return reader;
    }

    public Consumer<Exception> getExpConsumer() {
        return expConsumer;
    }
}
