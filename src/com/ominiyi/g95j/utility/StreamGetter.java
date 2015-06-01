package com.ominiyi.g95j.utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 *
 * @author Adedayo Ominiyi
 */
public abstract class StreamGetter extends Thread {

    protected ExecutableRunner executableRunner;
    protected boolean processed;
    private boolean streamInterrupted;
    private BufferedWriter bufferedPipedWriter;
    private BufferedReader bufferedPipedReader;
    private BufferedInputStream bufferedInputStream;

    public StreamGetter(InputStream in, ExecutableRunner executableRunner) {
        this.executableRunner = executableRunner;
        this.bufferedInputStream = new BufferedInputStream(in); // read from process
        PipedWriter pipedWriter = new PipedWriter();
        this.bufferedPipedWriter = new BufferedWriter(pipedWriter);
        try {
            this.bufferedPipedReader = new BufferedReader(new PipedReader(
                    pipedWriter)); // read from pipe
        } catch (Exception ex) {
            debugException(ex);
        }
        setDaemon(true);
    }

    /*public void setStreamInterrupted() {
        streamInterrupted = true;
    }*/

    public boolean getStreamInterrupted() {
        return this.streamInterrupted;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(this.bufferedInputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                yield();
                if (this.streamInterrupted) {
                    System.out.println("Stream interrupted!!");
                    closeStreams(bufferedReader, this.bufferedPipedWriter);
                    return;
                }
                this.processed = true;
                this.bufferedPipedWriter.write(line);
                this.bufferedPipedWriter.newLine();
                this.bufferedPipedWriter.flush();
                this.executableRunner.addOutputLine(this.bufferedPipedReader.readLine(),
                        this);
            }
            specializedRun();
            closeStreams(bufferedReader, this.bufferedPipedWriter);
        } catch (IOException ex) {
            debugException(ex);
        }
    }

    public void closeStreams(BufferedReader bufferedReader,
            BufferedWriter bufferedWriter) throws IOException {
        bufferedReader.close();
        bufferedWriter.close();
    }

    public abstract void specializedRun();
    
    private void debugException(Exception ex) {
        ex.printStackTrace();
    }
}