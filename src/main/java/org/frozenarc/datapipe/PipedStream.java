package org.frozenarc.datapipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Author: mpanchal
 * Date: 2022-12-03 15:31
 */
class PipedStream {

    private PipedOutputStream outputStream;
    private PipedInputStream inputStream;

    private static final Logger log = LoggerFactory.getLogger(PipedStream.class);

    public PipedStream() throws DataPipeException {
        try {
            inputStream = new PipedInputStream();
            outputStream = new PipedOutputStream(inputStream);
        } catch (IOException ex) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Error while closing input stream", e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("Error while closing output stream", e);
                }
            }
            throw new DataPipeException(ex);
        }
    }

    public void close() throws DataPipeException {
        try {
            outputStream.close();
            inputStream.close();
        } catch (IOException ex) {
            throw new DataPipeException(ex);
        }
    }

    public void closeAfterWrite(boolean error)  {
        try {
            outputStream.close();
            if (error) {
                inputStream.close();
            }
        } catch (IOException ex) {
            log.error("StreamWriter: Error during closing streams", ex);
        }
    }

    public void closeAfterRead(boolean error) {
        try {
            if (error) {
                outputStream.close();
            }
            inputStream.close();
        } catch (IOException ex) {
            log.error("StreamReader: Error during closing streams", ex);
        }
    }

    public PipedOutputStream getOutputStream() {
        return outputStream;
    }

    public PipedInputStream getInputStream() {
        return inputStream;
    }
}
