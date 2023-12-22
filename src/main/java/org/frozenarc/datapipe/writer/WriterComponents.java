package org.frozenarc.datapipe.writer;

import java.util.function.Consumer;

/*
 * Author: mpanchal
 * Date: 19-12-2023
 */
public class WriterComponents {

    private final StreamWriter writer;

    private final Consumer<Exception> expConsumer;

    public WriterComponents(StreamWriter writer, Consumer<Exception> expConsumer) {
        this.writer = writer;
        this.expConsumer = expConsumer;
    }

    public StreamWriter getWriter() {
        return writer;
    }

    public Consumer<Exception> getExpConsumer() {
        return expConsumer;
    }
}
