package org.frozenarc;

import org.frozenarc.datapipe.DataPipe;
import org.frozenarc.datapipe.DataPipeException;
import org.frozenarc.datapipe.joiner.JoinException;
import org.frozenarc.datapipe.reader.ReadException;
import org.frozenarc.datapipe.writer.WriteException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Author: mpanchal
 * Date: 2022-12-03 15:51
 */
public class Testing {

    @Test
    public void test() throws DataPipeException  {
        DataPipe.builder()
                .streamWriter(outputStream -> {
                    try {
                        InputStream inputStream = Testing.class.getResourceAsStream("/da-lmp-data.json");
                        inputStream.transferTo(outputStream);
                    } catch (IOException e) {
                        throw new WriteException(e);
                    }
                })
                .addStreamJoiner((inputStream, outputStream) -> {
                    try {
                        inputStream.transferTo(outputStream);
                    } catch (IOException e) {
                        throw new JoinException(e);
                    }
                })
                .streamReader(inputStream -> {
                    try {
                        inputStream.transferTo(System.out);
                    } catch (IOException e) {
                        throw new ReadException(e);
                    }
                })
                .build()
                .doStream();
    }
}
