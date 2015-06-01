package com.ominiyi.g95j.utility;

import java.io.InputStream;

/**
 *
 * @author Adedayo Ominiyi
 */
public class OutputStreamGetter extends StreamGetter {

    public OutputStreamGetter(InputStream in, ExecutableRunner executableRunner) {
        super(in, executableRunner);
        this.executableRunner.setOutput("");
    }

    @Override
    public void specializedRun() {
        this.executableRunner.setOutputMessage("\nProcess Terminated");
    }

    @Override
    public String toString() {
        return super.toString() + " OuputStreamGetter";
    }
}