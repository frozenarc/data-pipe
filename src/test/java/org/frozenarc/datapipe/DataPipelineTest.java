package org.frozenarc.datapipe;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataPipelineTest extends TestCase {

    private static void transferData(InputStream inputStream, OutputStream outputStream, boolean throwError) throws DataPipeException {
        if(throwError) {
            throw new DataPipeException("Hello");
        }
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
        InputStream fileStream = DataPipelineTest.class.getResourceAsStream("/sample.txt");
        DataPipeline.builder()
                    .sourceWriter(outputStream -> transferData(fileStream, outputStream, false))
                    .joiners((inputStream, outputStream) -> DataPipelineTest.transferData(inputStream, outputStream, false),
                             (inputStream, outputStream) -> DataPipelineTest.transferData(inputStream, outputStream, false))
                    .sinkReader(inputStream -> transferData(inputStream, System.out, false))
                    .build()
                    .stream();
    }

    /*public void testExceptionInWriter() throws DataPipeException {
        InputStream fileStream = DataPipelineTest.class.getResourceAsStream("/sample.txt");
        //noinspection ConstantConditions
        DataPipeline.builder()
                    .sourceWriter(outputStream -> transferData(fileStream, outputStream, true))
                    .joiners((inputStream, outputStream) -> DataPipelineTest.transferData(inputStream, outputStream, false),
                             (inputStream, outputStream) -> DataPipelineTest.transferData(inputStream, outputStream, false))
                    .sinkReader(inputStream -> transferData(inputStream, System.out, false))
                    .build()
                    .stream();
    }

    public void testExceptionInJoiner1() throws DataPipeException {
        InputStream fileStream = DataPipelineTest.class.getResourceAsStream("/sample.txt");
        //noinspection ConstantConditions
        DataPipeline.builder()
                    .sourceWriter(outputStream -> transferData(fileStream, outputStream, true))
                    .joiners((inputStream, outputStream) -> DataPipelineTest.transferData(inputStream, outputStream, true),
                             (inputStream, outputStream) -> DataPipelineTest.transferData(inputStream, outputStream, false))
                    .sinkReader(inputStream -> transferData(inputStream, System.out, false))
                    .build()
                    .stream();
    }

    public void testExceptionInJoiner2() throws DataPipeException {
        InputStream fileStream = DataPipelineTest.class.getResourceAsStream("/sample.txt");
        //noinspection ConstantConditions
        DataPipeline.builder()
                    .sourceWriter(outputStream -> transferData(fileStream, outputStream, false))
                    .joiners((inputStream, outputStream) -> DataPipelineTest.transferData(inputStream, outputStream, false),
                             (inputStream, outputStream) -> DataPipelineTest.transferData(inputStream, outputStream, true))
                    .sinkReader(inputStream -> transferData(inputStream, System.out, false))
                    .build()
                    .stream();
    }

    public void testExceptionInReader() throws DataPipeException {
        InputStream fileStream = DataPipelineTest.class.getResourceAsStream("/sample.txt");
        //noinspection ConstantConditions
        DataPipeline.builder()
                    .sourceWriter(outputStream -> transferData(fileStream, outputStream, false))
                    .joiners((inputStream, outputStream) -> DataPipelineTest.transferData(inputStream, outputStream, false),
                             (inputStream, outputStream) -> DataPipelineTest.transferData(inputStream, outputStream, false))
                    .sinkReader(inputStream -> transferData(inputStream, System.out, true))
                    .build()
                    .stream();
    }*/
}