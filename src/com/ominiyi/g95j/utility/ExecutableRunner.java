package com.ominiyi.g95j.utility;

import com.ominiyi.g95j.view.MainWindow;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

/**
 *
 * @author Adedayo Ominiyi
 */
public class ExecutableRunner {

    private List<Process> processes = null;
    private String output = null;
    private OutputStreamGetter outputStreamGetter = null;
    private ErrorStreamGetter errorStreamGetter = null;
    private StringBuffer exitProcessMessage = null;
    private MainWindow frame = null;
    private boolean flag = false;
    private boolean errors = false;
    
    private static class ExecutableRunnerHelper {
        public final static ExecutableRunner executableRunner = new ExecutableRunner(MainWindow.instanceOf());
    }

    private ExecutableRunner(MainWindow frame) {
        this.processes = Collections.synchronizedList(new ArrayList<Process>(2));

        this.frame = frame;
        this.exitProcessMessage = new StringBuffer("");
    }

    public static ExecutableRunner instanceOf(MainWindow frame) {
        ExecutableRunner executableRunner = ExecutableRunnerHelper.executableRunner;
        return executableRunner;
    }

    public void execute(Process process) {
        this.processes.add(process);

        this.outputStreamGetter = new OutputStreamGetter(process.getInputStream(),
                this);
        this.errorStreamGetter = new ErrorStreamGetter(process.getErrorStream(),
                this);
        this.outputStreamGetter.start();
        this.errorStreamGetter.start();
    }

    public synchronized void addOutputLine(String line,
            StreamGetter streamGetter) {
        try {
            if (streamGetter instanceof OutputStreamGetter) {
                setOutput(line + "\n");
                this.notifyGui();
            } else if (streamGetter instanceof ErrorStreamGetter) {
                this.exitProcessMessage.append(line).append("\n");
                notifyGuiForErrors(line);
            }
        } catch (Exception ex) {
            debugException(ex);
        }
    }

    public synchronized void notifyGui() {
        this.frame.updateRunOutput();
    }

    public synchronized void notifyGuiForErrors(String errors) {
        this.frame.updateForError(errors);
    }

    public synchronized void setOutput(String output) {
        this.output = output;
    }

    public synchronized String getOutput() {
        return this.output;
    }

    protected synchronized void setExitProcessMessage(
            StringBuffer exitProcessMessage) {
        this.exitProcessMessage = exitProcessMessage;
    }

    public synchronized String getExitProcessMessage() {
        return this.exitProcessMessage.toString();
    }

    public void processInput(String input) {
        if (isProcessing()) {
            try {
                ((Process) this.processes.get(this.processes.size() - 1)).getOutputStream().write(
                        input.getBytes());
                ((Process) this.processes.get(this.processes.size() - 1)).getOutputStream().flush();
            } catch (Exception ex) {
                debugException(ex);
            }
        }
    }

    public boolean isProcessing() {
        return (this.processes.size() > 0);
    }

    public synchronized void setNoErrorMessage(String text) {
        try {
            notify();
        } catch (Exception ex) {
            debugException(ex);
        }
        this.flag = true;
        this.errors = false;
    }

    public synchronized void setErrorMessage(String text) {
        try {
            notify();
        } catch (Exception ex) {
            debugException(ex);
        }
        this.flag = true;
        this.errors = true;
    }

    public synchronized void setOutputMessage(String text) {
        if (!this.flag) {
            try {
                wait();
            } catch (Exception ex) {
                debugException(ex);
            }
        }
        if (errors) {
            text += "... there were problems.";
            notifyGuiForErrors(text);
        }
        this.flag = false;
        this.errors = false;
        this.processes.clear();
    }
    
    private void debugException(Exception ex) {
        ex.printStackTrace();
    }
}