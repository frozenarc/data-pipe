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
            if (outputStream != null) {
                try {
                    log.debug("constructor: error occurred: closing input stream");
                    outputStream.close();
                    log.debug("constructor: output stream closed");
                } catch (IOException e) {
                    log.error("constructor: error while closing output stream", e);
                }
            }
            if (inputStream != null) {
                try {
                    log.debug("constructor: error occurred: closing input stream");
                    inputStream.close();
                    log.debug("constructor: input stream closed");
                } catch (IOException e) {
                    log.error("constructor: error while closing input stream", e);
                }
            }
            throw new DataPipeException(ex);
        }
    }

    public void close() throws DataPipeException {
        try {
            log.debug("close: closing output stream");
            outputStream.close();
            log.debug("close: closing input stream");
            inputStream.close();
            log.debug("close: streams closed without error");
        } catch (IOException ex) {
            throw new DataPipeException(ex);
        }
    }

    public void closeAfterWrite(boolean error) {
        try {
            log.debug("closeAfterWrite: closing output stream");
            outputStream.close();
            if (error) {
                log.debug("closeAfterWrite: if(error) : closing input stream");
                inputStream.close();
            }
            log.debug("closeAfterWrite: stream(s) closed without error");
        } catch (IOException ex) {
            log.error("StreamWriter: Error during closing streams", ex);
        }
    }

    public void closeAfterRead(boolean error) {
        try {
            if (error) {
                log.debug("closeAfterRead: if(error) : closing output stream");
                outputStream.close();
            }
            log.debug("closeAfterRead: closing input stream");
            inputStream.close();
            log.debug("closeAfterRead: stream(s) closed without error");
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
