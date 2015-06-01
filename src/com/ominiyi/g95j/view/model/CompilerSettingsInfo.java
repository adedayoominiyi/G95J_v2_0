package com.ominiyi.g95j.view.model;

/**
 *
 * @author Adedayo Ominiyi
 */
public class CompilerSettingsInfo {

    private String pathToG95Compiler = null;
    private String pathToCompilationOutputFolder = null;
    private boolean overWritePreviousOutput = true;

    public String getPathToG95Compiler() {
        if (pathToG95Compiler != null) {
            pathToG95Compiler = pathToG95Compiler.trim();
        }
        return pathToG95Compiler;
    }

    public String getPathToCompilationOutputFolder() {
        if (pathToCompilationOutputFolder != null) {
            pathToCompilationOutputFolder = pathToCompilationOutputFolder.trim();
        }
        return pathToCompilationOutputFolder;
    }

    public boolean isOverWritePreviousOutput() {
        return overWritePreviousOutput;
    }

    public void setPathToG95Compiler(String value) {
        if ((value != null) && (value.trim().equals(""))) {
            value = "";
        }
        pathToG95Compiler = value;
    }

    public void setPathToCompilationOutputFolder(String value) {
        if ((value != null) && (value.trim().equals(""))) {
            value = "";
        }
        pathToCompilationOutputFolder = value;
    }

    

    public void setOverWritePreviousOutput(boolean value) {
        this.overWritePreviousOutput = value;
    }
}