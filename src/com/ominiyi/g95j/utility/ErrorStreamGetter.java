package com.ominiyi.g95j.utility;

import java.io.InputStream;

/**
 *
 * @author Adedayo Ominiyi
 */
public class ErrorStreamGetter extends StreamGetter {

    public ErrorStreamGetter(InputStream in,
            ExecutableRunner executableRunner) {
        super(in, executableRunner);
        this.executableRunner.setExitProcessMessage(new StringBuffer());
    }

    @Override
    public void specializedRun() {
        if (!getStreamInterrupted()) {
            if (!processed) {
                this.executableRunner.setNoErrorMessage(
                        "\nProcess Terminated with no errors.");
            } else {
                this.executableRunner.setErrorMessage(
                        "\nProcess Terminated ... there were problems.");
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + " ErrorStreamGetter";
    }
}