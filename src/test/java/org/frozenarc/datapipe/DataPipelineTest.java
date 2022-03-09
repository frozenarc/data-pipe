package org.frozenarc.datapipe;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataPipelineTest extends TestCase {

    private static void transferData(InputStream inputStream, OutputStream outputStream) throws DataPipeException {
        try {
            int read;
            byte[] chunk = new byte[32];
            while ((read = inputStream.read(chunk)) != -1) {
                outputStream.write(chunk, 0, read);
            }
            inputStream.close();
        } catch (IOException ex) {
            throw new DataPipeException(ex);
        }
    }

    public void testStream() throws DataPipeException {
        InputStream inputStream = DataPipelineTest.class.getResourceAsStream("/sample.txt");
        //noinspection ConstantConditions
        DataPipeline.builder()
                    .sourceWriter(outputStream -> transferData(inputStream, outputStream))
                    .joiners(DataPipelineTest::transferData,
                             DataPipelineTest::transferData)
                    .sinkReader(inputStream1 -> transferData(inputStream1, System.out))
                    .build()
                    .stream();
    }
}