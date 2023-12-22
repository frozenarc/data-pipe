package org.frozenarc.datapipe.joiner;

import java.util.function.Consumer;

/*
 * Author: mpanchal
 * Date: 19-12-2023
 */
public class JoinerComponents {

    private final StreamJoiner joiner;

    private final Consumer<Exception> expConsumer;

    public JoinerComponents(StreamJoiner joiner, Consumer<Exception> expConsumer) {
        this.joiner = joiner;
        this.expConsumer = expConsumer;
    }

    public StreamJoiner getJoiner() {
        return joiner;
    }

    public Consumer<Exception> getExpConsumer() {
        return expConsumer;
    }
}
